package com.example.notesappretrofit.data.repository


import android.util.Log
import com.example.notesappretrofit.data.local.dao.NoteDao
import com.example.notesappretrofit.data.local.database.NoteDatabase
import com.example.notesappretrofit.data.local.entity.NoteEntity
import com.example.notesappretrofit.data.local.token.DataAssetManager
import com.example.notesappretrofit.data.mappers.toDeleteEntity
import com.example.notesappretrofit.data.mappers.toNote
import com.example.notesappretrofit.data.mappers.toNoteEntity
import com.example.notesappretrofit.data.mappers.toNoteRequest
import com.example.notesappretrofit.data.remote.note.NoteApi
import com.example.notesappretrofit.data.remote.note.dto.NoteRequest
import com.example.notesappretrofit.domain.NoteError
import com.example.notesappretrofit.domain.Result
import com.example.notesappretrofit.domain.entity.Note
import com.example.notesappretrofit.domain.repository.NoteRepository
import com.example.notesappretrofit.utils.generateTempId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

import javax.inject.Inject

 class NoteRepositoryImpl @Inject constructor(
    private val api : NoteApi,
    private val local:NoteDao,
    private val localDatabase : NoteDatabase,
    private val dataAssetManager:DataAssetManager
):NoteRepository {

     override suspend fun createNote(
         request: NoteRequest
     ): Result<Note, NoteError> {

         return try {
             val noteEntity = request.toNoteEntity().copy(hasSynced = false)
             local.upsertNote(noteEntity)
             Log.d("createNote",noteEntity.toString())
             Result.Success(noteEntity.toNote()!!)
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }

     }

     override suspend fun deleteAllData(){
         localDatabase.clearAllTables()
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
             Log.d("upDatedNote",noteEntity.toString())
             local.updateNote(noteEntity)
             Result.Success(true)
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     override suspend fun deleteNote(noteId: Int): Result<Boolean, NoteError> {
         return try {
             val existingNote = local.getNoteById(noteId).firstOrNull()
                 ?:return Result.Error(NoteError.NOTE_NOT_FOUND)
             //save in deleted cache
             if(existingNote != null){
                 local.insertDeletedNote(existingNote.toDeleteEntity())
                 //delete from local
                 local.deleteNote(noteId)
             }

             Result.Success(true)
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     override  fun getAllNotes(): Result<Flow<List<Note>>, NoteError> {
         return try {
             val notes = local.getAllNotes().map {
                 entities ->
                     entities.map {
                         it.toNote()!!
                         }
                 }
             Result.Success(notes)
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     override  fun getNoteById(id: Int): Result<Flow<Note>, NoteError> {
         return try {
             val note = local.getNoteById(id).map { it.toNote()!! }
             Result.Success(note)
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     override suspend fun syncNotes(): Result<Boolean, NoteError> {
         try {
            val token = dataAssetManager.getToken()?: return Result.Error(NoteError.UNAUTHORIZED)
            val unsyncedNotes = local.getUnsyncedNotes().first()
             for (note in unsyncedNotes) {
                 Log.d("SyncNotes",note.toString())
                 if (note.id < 0) {//negative temp id
                     //new note
                     val response = api.createNote(note.toNoteRequest(), token)
                     val updatedNote = note.copy(id = response.noteDetails.id, hasSynced = true)
                     local.updateNote(updatedNote)
                 } else {
                     // Existing note
                     Log.d("SyncNotes",note.toNoteRequest().toString())
                     api.updateNote(note.toNoteRequest(), note.id.toString(), token)
                     val updatedNote = note.copy(hasSynced = true)
                     local.updateNote(updatedNote)
                 }
             }
             val deletedNotes = local.getDeletedNotes().first()
             for(note in deletedNotes){
                 //delete from remote
               api.deleteNote(id = note.toString(),token= token)
                 //delte from local delted cache
               local.deleteDeletedNote(note)
             }
             //remote notes and local notes
             val remoteNotes = api.getAllNotes(token).notes
             val localNotes = local.getAllNotes().first()

             // Prepare lists for batch operations
             val notesToInsert = mutableListOf<NoteEntity>()
             val notesToUpdate = mutableListOf<NoteEntity>()
             val idsToDelete = mutableListOf<Int>()

             remoteNotes.forEach { remote ->
                 val localNote = localNotes.find { it.id == remote.id }
                 if (localNote == null) {
                     notesToInsert.add(remote.toNoteEntity())
                 } else if (remote.toNoteEntity() != localNote) {
                     notesToUpdate.add(remote.toNoteEntity())
                 }
             }
             localNotes.forEach { local ->
                 if (remoteNotes.none { it.id == local.id }) {
                     idsToDelete.add(local.id)
                 }
             }
             //patch operation
             local.upsertNotes(notesToInsert + notesToUpdate)
             //delete which are deleted already in remote but not in local
             local.deleteNotesByIds(idsToDelete)

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
         Log.d("home output",e.message!! +" "+e.stackTrace + e.toString())
         return when (e.code()) {
             401, 403 -> Result.Error(NoteError.UNAUTHORIZED)
             400 -> Result.Error(NoteError.INVALID_INPUT)
             500 -> Result.Error(NoteError.SERVER_ERROR)
             else -> Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

 }




