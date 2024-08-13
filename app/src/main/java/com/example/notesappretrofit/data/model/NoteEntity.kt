package com.example.notesappretrofit.data.model

import java.time.LocalDate

data class NoteEntity(
    val title : String,
    val description : String,
    val date : LocalDate
)
