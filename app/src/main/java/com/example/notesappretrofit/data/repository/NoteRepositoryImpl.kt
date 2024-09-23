package com.example.notesappretrofit.data.repository


import com.example.notesappretrofit.data.local.dao.NoteDao
import com.example.notesappretrofit.data.local.token.TokenManager
import com.example.notesappretrofit.data.mappers.toDeleteEntity
import com.example.notesappretrofit.data.mappers.toNote
import com.example.notesappretrofit.data.mappers.toNoteEntity
import com.example.notesappretrofit.data.mappers.toNoteRequest
import com.example.notesappretrofit.data.remote.model.SyncOperation
import com.example.notesappretrofit.data.remote.note.NoteApi
import com.example.notesappretrofit.data.remote.note.dto.NoteRequest
import com.example.notesappretrofit.domain.NoteError
import com.example.notesappretrofit.domain.Result
import com.example.notesappretrofit.domain.entity.Note
import com.example.notesappretrofit.domain.repository.NoteRepository
import com.example.notesappretrofit.presentation.home.elements.ConnectivityObserver
import com.example.notesappretrofit.presentation.home.viewModel.UiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

import javax.inject.Inject

 class NoteRepositoryImpl @Inject constructor(
    private val api : NoteApi,
    private val local:NoteDao,
    private val tokenManager:TokenManager
):NoteRepository {

     override suspend fun createNote(
         request: NoteRequest
     ): Result<Note, NoteError> {

         return try {
             val noteEntity = request.toNoteEntity().copy(hasSynced = false)
             local.upsertNote(noteEntity)
             Result.Success(noteEntity.toNote())
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }

     }


     override suspend fun updateNote(
         request: NoteRequest,
         noteId: Int
     ): Result<Boolean, NoteError> {
         return try {
             val existingNote = local.getNoteById(noteId).firstOrNull()
                 ?: return Result.Error(NoteError.NOTE_NOT_FOUND)
             val noteEntity = existingNote.copy(
                 hasSynced = false,
                 title = request.title,
                 description = request.description,
                 isFavorite = request.isFavorite,
                 isLocked = request.isLocked
             )
             local.upsertNote(noteEntity)
             Result.Success(true)
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     override suspend fun deleteNote(noteId: Int): Result<Boolean, NoteError> {
         return try {
             val existingNote = local.getNoteById(noteId).firstOrNull()
                 ?:return Result.Error(NoteError.NOTE_NOT_FOUND)
             local.insertDeletedNote(existingNote.toDeleteEntity())
             local.deleteNote(noteId)
             Result.Success(true)
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     override suspend fun getAllNotes(): Result<Flow<List<Note>>, NoteError> {
         return try {
             val notes = local.getAllNotes().map {
                 entities ->
                     entities.map {
                         it.toNote()
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

     override suspend fun syncNotes(): Result<Boolean, NoteError> {
         try {
            val token = tokenManager.getToken()?: return Result.Error(NoteError.UNAUTHORIZED)
            val unsyncedNotes = local.getUnsyncedNotes().first()
             for (note in unsyncedNotes) {
                 if (note.id == 0) {
                     //new note
                     val response = api.createNote(note.toNoteRequest(), token)
                     val updatedNote = note.copy(id = response.noteDetails.id, hasSynced = true)
                     local.upsertNote(updatedNote)
                 } else {
                     // Existing note
                     api.updateNote(note.toNoteRequest(), note.id.toString(), token)
                     val updatedNote = note.copy(hasSynced = true)
                     local.upsertNote(updatedNote)
                 }
             }
             val deletedNotes = local.getDeletedNotes().first()
             for(note in deletedNotes){
               api.deleteNote(id = note.toString(),token= token)
               local.deleteDeletedNote(note)
             }

             val remoteNotes = api.getAllNotes(token).notes

             val localNotes = local.getAllNotes().first()

             val notesToInsert = remoteNotes.filter { remote ->
                 localNotes.none { local -> local.id == remote.id }
             }
             val notesToUpdate = remoteNotes.filter { remote ->
                 localNotes.any { local ->
                     local.id == remote.id
                             &&
                     (local.title != remote.title || local.description != remote.description
                     || local.isLocked != remote.isLocked || local.isFavorite != remote.isFavorite)
                 }
             }
             val notesToDelete = localNotes.filter { local ->
                 remoteNotes.none { remote -> remote.id == local.id }
             }

             notesToInsert.forEach { local.upsertNote(it.toNoteEntity()) }
             notesToUpdate.forEach { local.upsertNote(it.toNoteEntity()) }
             notesToDelete.forEach { local.deleteNote(it.id) }
             return Result.Success(true)
         } catch (e: IOException) {
             return Result.Error(NoteError.NETWORK_ERROR)
         } catch (e: HttpException) {
             return handleHttpException(e)
         } catch (e: Exception) {
             return Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     private fun handleHttpException(e: HttpException): Result<Boolean, NoteError> {
         return when (e.code()) {
             401, 403 -> Result.Error(NoteError.UNAUTHORIZED)
             400 -> Result.Error(NoteError.INVALID_INPUT)
             500 -> Result.Error(NoteError.SERVER_ERROR)
             else -> Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

 }




