package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [DatabaseAsteroid::class], version = 1, exportSchema = false)
abstract class DatabaseManager:RoomDatabase() {
    abstract val asteroidDAO:AsteroidDAO

    companion object{
        @Volatile
        private lateinit var DATABASE:DatabaseManager

        fun getInstance(context: Context) :DatabaseManager{
            synchronized(this){
                if (!::DATABASE.isInitialized){
                    DATABASE= Room
                        .databaseBuilder(context.applicationContext,DatabaseManager::class.java,"Asteroid_Database")
                        .build()
                }
                return DATABASE
            }
        }
    }
}