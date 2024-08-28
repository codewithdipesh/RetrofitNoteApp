package com.example.notesappretrofit.domain.repository

import com.example.notesappretrofit.data.remote.note.NoteApi
import com.example.notesappretrofit.data.remote.note.dto.GetAllNotesResponse
import com.example.notesappretrofit.data.remote.note.dto.NoteCreateResponse
import com.example.notesappretrofit.data.remote.note.dto.NoteData
import com.example.notesappretrofit.data.remote.note.dto.NoteRequest
import com.example.notesappretrofit.data.remote.note.dto.NoteUpdatedDeletedResponse
import com.example.notesappretrofit.domain.NoteError
import com.example.notesappretrofit.domain.Result
import javax.inject.Inject

interface NoteRepository {
    suspend fun createNote(request : NoteRequest,token :String) : Result<NoteData,NoteError>
    suspend fun updateNote(request : NoteRequest,noteId: String,token: String) : Result<Boolean,NoteError>
    suspend fun deleteNote(noteId: String,token: String) : Result<Boolean,NoteError>
    suspend fun getAllNotes(token: String):Result<List<NoteData>,NoteError>
    suspend fun getNoteById(token: String,id:String):Result<NoteData,NoteError>
}