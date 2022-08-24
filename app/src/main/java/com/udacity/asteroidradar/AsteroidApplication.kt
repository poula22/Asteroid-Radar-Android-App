package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication:Application() {
    val applicationScope= CoroutineScope(Dispatchers.Default)
    fun delayedInit(){
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constrains= Constraints.Builder()
            .setRequiredNetworkType(NetworkType.METERED)
            .setRequiresCharging(true)
            .build()
        val repeatingRequest= PeriodicWorkRequestBuilder<RefreshDataWorker>(
            1L,
            TimeUnit.DAYS
        )
            .setConstraints(constrains)
            .build()

        WorkManager.getInstance()
            .enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }
}