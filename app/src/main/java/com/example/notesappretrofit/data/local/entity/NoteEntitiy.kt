package com.example.notesappretrofit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class NoteEntity(
    @PrimaryKey
    val id: Int,
    val createdAt: String,
    val description: String,
    val title: String,
    val isLocked : Boolean,
    val isFavorite : Boolean,
    val hasSynced: Boolean
)