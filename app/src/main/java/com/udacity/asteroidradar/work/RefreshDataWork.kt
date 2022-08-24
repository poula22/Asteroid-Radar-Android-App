package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.DatabaseManager
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params)  {
    companion object{
        const val WORK_NAME="RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = DatabaseManager.getInstance(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            repository.refreshAsteroidList()
            repository.deletePreviousDayData()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}