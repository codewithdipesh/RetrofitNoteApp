package com.example.notesappretrofit

import android.app.Application
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.notesappretrofit.data.workmanager.NoteSyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class NoteApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setUpPreodicSync()
    }

    private fun setUpPreodicSync(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicSyncRequest = PeriodicWorkRequestBuilder<NoteSyncWorker>(15,
            TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "NoteSync",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicSyncRequest
        )
    }



}