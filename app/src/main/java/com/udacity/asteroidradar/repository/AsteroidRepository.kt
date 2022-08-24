package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.DatabaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class AsteroidRepository(val database: DatabaseManager) {

    val asteroidList:LiveData<List<Asteroid>> = Transformations.map(database.asteroidDAO.getAll()){
        it.toAsteroid()
    }




    suspend fun deletePreviousDayData(){
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val date = dateFormat.format(calendar.time)
        database.asteroidDAO.deletePreviousDayData(date)
    }
    suspend fun refreshAsteroidList(){
        withContext(Dispatchers.Default){
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            val startDate = dateFormat.format(calendar.time)
            println(startDate.toString())
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            val endDate=dateFormat.format(calendar.time)
            val asteroidList= parseAsteroidsJsonResult(
                JSONObject(
                    AsteroidApi.asteroidWebService.getAsteroids(
                        startDate.toString(),
                        endDate.toString()
                    )
                )


            )
            database.asteroidDAO.insertAll(
                *asteroidList.toDatabaseAsteroid()
            )
        }
    }


    suspend fun getImageOfTheDay(): PictureOfDay {
        return AsteroidApi.asteroidWebService.getImageOfTheDay()
    }
}