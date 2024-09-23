package com.example.notesappretrofit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "deleted_notes")
data class DeletedNoteEntity(
    @PrimaryKey
    val id: Int,
    val createdAt: String,
    val description: String,
    val title: String,
    val isLocked : Boolean,
    val isFavorite : Boolean
)