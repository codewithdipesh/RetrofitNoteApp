package com.example.notesappretrofit.domain.repository

import com.example.notesappretrofit.data.remote.note.dto.NoteDto
import com.example.notesappretrofit.data.remote.note.dto.NoteRequest
import com.example.notesappretrofit.domain.NoteError
import com.example.notesappretrofit.domain.Result

interface NoteRepository {
    suspend fun createNote(request : NoteRequest,token :String) : Result<NoteDto,NoteError>
    suspend fun updateNote(request : NoteRequest,noteId: String,token: String) : Result<Boolean,NoteError>
    suspend fun deleteNote(noteId: String,token: String) : Result<Boolean,NoteError>
    suspend fun getAllNotes(token: String):Result<List<NoteDto>,NoteError>
    suspend fun getNoteById(token: String,id:String):Result<NoteDto,NoteError>
}