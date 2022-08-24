package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroid: DatabaseAsteroid)

    @Query("SELECT * FROM DatabaseAsteroid ORDER BY  closeApproachDate ASC")
    fun getAll():LiveData<List<DatabaseAsteroid>>

    @Query("DELETE FROM DatabaseAsteroid WHERE closeApproachDate= :date")
    suspend fun deletePreviousDayData(date:String)
}