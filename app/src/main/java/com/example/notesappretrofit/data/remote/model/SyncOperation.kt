package com.example.notesappretrofit.data.remote.model

import com.example.notesappretrofit.data.remote.note.dto.NoteRequest

sealed class SyncOperation {
    data class createNote(val note:NoteRequest):SyncOperation()
    data class updateNote(val note:NoteRequest):SyncOperation()
    data class deleteNote(val noteId:Int):SyncOperation()
}