package com.example.notesappretrofit.data.remote.note.dto

import com.google.gson.annotations.SerializedName

data class NoteUpdatedDeletedResponse(
    @SerializedName("data")
    val Isupdated: Boolean,
    val message: String,
    val status: Int
)