package com.example.notesappretrofit.data.repository


import com.example.notesappretrofit.data.local.dao.NoteDao
import com.example.notesappretrofit.data.mappers.toNote
import com.example.notesappretrofit.data.mappers.toNoteEntity
import com.example.notesappretrofit.data.remote.model.SyncOperation
import com.example.notesappretrofit.data.remote.note.NoteApi
import com.example.notesappretrofit.data.remote.note.dto.NoteRequest
import com.example.notesappretrofit.domain.NoteError
import com.example.notesappretrofit.domain.Result
import com.example.notesappretrofit.domain.entity.Note
import com.example.notesappretrofit.domain.repository.NoteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import retrofit2.HttpException
import java.io.IOException

import javax.inject.Inject

 class NoteRepositoryImpl @Inject constructor(
    private val api : NoteApi,
     private val local:NoteDao
):NoteRepository {

     private val syncQueue = MutableStateFlow<List<SyncOperation>>(emptyList())



     override suspend fun createNote(
         request: NoteRequest
     ): Result<Note, NoteError> {

         return try {
                 val noteEntity = request.toNoteEntity().copy(hasSynced = false)
                 local.upsertNote(noteEntity)
                 queueOperation(SyncOperation.createNote(request))
                 Result.Success(noteEntity.toNote())
         } catch(e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }

         }


     override suspend fun updateNote(
         request: NoteRequest,
         noteId: Int
     ): Result<Boolean, NoteError> {
         return try {
             val noteEntity = request.toNoteEntity().copy(hasSynced = false)
             local.upsertNote(noteEntity)
             queueOperation(SyncOperation.updateNote(request))
             Result.Success(true)
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     override suspend fun deleteNote(noteId: Int): Result<Boolean, NoteError> {
         return try {
             local.deleteNote(noteId)
             queueOperation(SyncOperation.deleteNote(noteId))
             Result.Success(true)
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     override suspend fun getAllNotes(): Result<Flow<List<Note>>, NoteError> {
         return try {
             val notes = local.getAllNotes()
                 .map { entities ->
                    entities
                        .map { it.toNote()
                 }
             }
             Result.Success(notes)
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     override suspend fun getNoteById(id: Int): Result<Flow<Note>, NoteError> {
         return try {
              val note = local.getNoteById(id).map { it.toNote() }
              Result.Success(note)
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     override suspend fun syncNotes(token: String): Result<Boolean, NoteError> {
         // Process local sync queue first (if any)
         if (syncQueue.value.isNotEmpty()) {
             val result = processQueue(token)
             if (result is Result.Error) {
                 // If processing the queue fails, return early without fetching remote data
                 return result
             }
         }

         //Fetch all notes from the remote server
         try {
             val remoteResult = api.getAllNotes(token).notes

             if (remoteResult.isNotEmpty()) {
                 remoteResult.forEach { noteDto ->
                     local.upsertNote(noteDto.toNoteEntity())
                 }
             }
             return Result.Success(true)

         } catch (e: IOException) {
             // Handle no internet connection
             return Result.Error(NoteError.NETWORK_ERROR)
         } catch (e: HttpException) {
             // Handle HTTP errors
             when (e.code()) {
                 401, 403 -> return Result.Error(NoteError.UNAUTHORIZED)
                 400 -> return Result.Error(NoteError.INVALID_INPUT)
                 500 -> return Result.Error(NoteError.SERVER_ERROR)
                 else -> return Result.Error(NoteError.UNKNOWN_ERROR)
             }
         } catch (e: Exception) {
             // Handle unexpected errors
             return Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     private suspend fun processQueue(token: String): Result<Boolean, NoteError> {
         val maxRetries = 3 // Set a limit for retries
         val retryDelayMillis = 2000L // Delay between retries

         syncQueue.value.forEach { operation ->
             var currentRetry = 0
             var success = false

             while (currentRetry < maxRetries && !success) {
                 try {
                     // Execute the sync operation
                     when (operation) {
                         is SyncOperation.deleteNote -> {
                             api.deleteNote(
                                 operation.noteId.toString(),
                                 token
                             )
                         }

                         is SyncOperation.createNote -> {
                             api.createNote(
                                 operation.note,
                                 token
                             )
                             local.upsertNote(operation.note.toNoteEntity().copy(hasSynced = true))
                         }

                         is SyncOperation.updateNote -> {
                             api.updateNote(
                                 operation.note,
                                 operation.note.id.toString(),
                                 token
                             )
                             local.upsertNote(operation.note.toNoteEntity().copy(hasSynced = true))
                         }
                     }

                     // If we reach this point, the operation succeeded
                     success = true
                     //update the syncQueue
                     syncQueue.update { it - operation }

                 } catch (e: IOException) {
                     // No Internet
                     return Result.Error(NoteError.NETWORK_ERROR)

                 } catch (e: HttpException) {
                     // Custom Error from Backend
                     when (e.code()) {
                         401, 403 -> return Result.Error(NoteError.UNAUTHORIZED)
                         400 -> return Result.Error(NoteError.INVALID_INPUT)
                         500 -> {
                             currentRetry++
                             if (currentRetry < maxRetries) {
                                 delay(retryDelayMillis) // Wait before retrying
                             } else {
                                 return Result.Error(NoteError.SERVER_ERROR)
                             }
                         }
                         else -> return Result.Error(NoteError.UNKNOWN_ERROR)
                     }

                 } catch (e: Exception) {
                     // For unknown errors, retry if maxRetries is not exceeded
                     currentRetry++
                     if (currentRetry < maxRetries) {
                         delay(retryDelayMillis) // Wait before retrying
                     } else {
                         return Result.Error(NoteError.UNKNOWN_ERROR)
                     }
                 }
             }

             if (!success) {
                 // If we still haven't succeeded after maxRetries, return an error
                 return Result.Error(NoteError.UNKNOWN_ERROR)
             }
         }

         // If we reach here, all operations succeeded
         return Result.Success(true)
     }




     private fun queueOperation(operation: SyncOperation) {
         syncQueue.update { it + operation }
     }

 }




