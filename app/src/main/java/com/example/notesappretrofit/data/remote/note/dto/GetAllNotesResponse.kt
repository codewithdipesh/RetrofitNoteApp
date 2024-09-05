package com.example.notesappretrofit.data.remote.note.dto

import com.google.gson.annotations.SerializedName

data class GetAllNotesResponse(
    @SerializedName("data")
    val notes: List<NoteDto>,
    val message: String,
    val status: Int
)