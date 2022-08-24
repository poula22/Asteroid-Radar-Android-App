package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.DatabaseManager
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val database= DatabaseManager.getInstance(app.applicationContext)
    private val repository= AsteroidRepository(database)
    private val _nasaImage:MutableLiveData<PictureOfDay> = MutableLiveData()
    val nasaImage:LiveData<PictureOfDay>
    get() = _nasaImage
    val asteroidList:LiveData<List<Asteroid>> = Transformations.map(repository.asteroidList){
        it
    }
    init {
        viewModelScope.launch {
            try {
                repository.refreshAsteroidList()
            }catch (ex:Exception){

            }
        }
        getNasaImage()
    }

    fun getNasaImage(){
        viewModelScope.launch {
            try {
                val result=repository.getImageOfTheDay()
                Log.v("test::::",result.mediaType)
                if (result.mediaType=="image"){
                    _nasaImage.value=result
                }
            }catch (ex:Exception){

            }

        }
    }

    fun startDate():String{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun endDate():String{
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR,7)
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}

class Factory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}