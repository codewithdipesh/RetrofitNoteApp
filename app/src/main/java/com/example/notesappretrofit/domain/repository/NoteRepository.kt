package com.example.notesappretrofit.domain.repository

import com.example.notesappretrofit.data.remote.note.dto.NoteDto
import com.example.notesappretrofit.data.remote.note.dto.NoteRequest
import com.example.notesappretrofit.domain.NoteError
import com.example.notesappretrofit.domain.Result
import com.example.notesappretrofit.domain.entity.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun createNote(request : NoteRequest) : Result<Note,NoteError>
    suspend fun updateNote(request : NoteRequest,noteId: Int) : Result<Boolean,NoteError>
    suspend fun deleteNote(noteId: Int) : Result<Boolean,NoteError>
    fun getAllNotes():Result<Flow<List<Note>>,NoteError>
    fun getNoteById(id:Int):Result<Flow<Note>,NoteError>
    suspend fun deleteAllData()

    suspend fun syncNotes():Result<Boolean,NoteError>
}