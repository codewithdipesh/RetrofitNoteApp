package com.example.notesappretrofit.data.remote.note.dto

data class NoteRequest(
    val id :Int = 0,
    val description: String,
    val title: String,
    val isFavorite : Boolean,
    val isLocked : Boolean,
    val createdAt : String
)