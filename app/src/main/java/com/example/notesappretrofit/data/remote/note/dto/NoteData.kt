package com.example.notesappretrofit.data.remote.note.dto

data class NoteData(
    val createdAt: String,
    val description: String,
    val id: Int,
    val title: String,
    val isLocked : Boolean,
    val isFavorite : Boolean
)