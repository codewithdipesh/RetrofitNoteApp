package com.example.notesappretrofit.data.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.notesappretrofit.domain.Result.Success
import com.example.notesappretrofit.domain.Result.Error
import com.example.notesappretrofit.domain.repository.NoteRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject

class NoteSyncWorker @Inject constructor(
     context: Context,
     params: WorkerParameters,
    private val repository: NoteRepository
) : CoroutineWorker(context,params) {

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