package com.example.notesappretrofit.domain.entity



data class Note(
    val id: Int,
    val createdAt: String,
    val description: String,
    val title: String,
    val isLocked : Boolean,
    val isFavorite : Boolean,
    val hasSynced :Boolean
)