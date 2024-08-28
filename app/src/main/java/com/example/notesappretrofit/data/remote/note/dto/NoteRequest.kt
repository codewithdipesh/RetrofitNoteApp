package com.example.notesappretrofit.data.remote.note.dto

data class NoteRequest(
    val description: String,
    val title: String,
    val isFavorite : Boolean,
    val isLocked : Boolean
)