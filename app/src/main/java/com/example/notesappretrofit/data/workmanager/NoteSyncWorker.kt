package com.example.notesappretrofit.data.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.notesappretrofit.domain.Result.Success
import com.example.notesappretrofit.domain.Result.Error
import com.example.notesappretrofit.domain.repository.NoteRepository
import javax.inject.Inject

class NoteSyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context,params) {

    @Inject
    lateinit var repository: NoteRepository

    override suspend fun doWork(): Result {
        return when (repository.syncNotes()) {
            is Success ->{
                Result.success()
            }
            is Error->{
                Result.retry()
            }
        }
    }
}