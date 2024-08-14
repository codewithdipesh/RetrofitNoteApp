package com.example.notesappretrofit.data.remote.note.dto

import com.google.gson.annotations.SerializedName

data class NoteCreateResponse(
    @SerializedName("data")
    val noteDetails : NoteData,
    val message: String,
    val status: Int
)