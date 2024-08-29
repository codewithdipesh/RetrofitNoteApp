package com.example.notesappretrofit.presentation.add_edit.viewmodel

import com.example.notesappretrofit.utils.getCurrentDate
import java.time.LocalDate

data class NoteUi(
    var title:String = "",
    val description:String = "",
    val isFavorite:Boolean = false,
    val isLocked:Boolean = false,
    val createdAt:String = getCurrentDate()
)
