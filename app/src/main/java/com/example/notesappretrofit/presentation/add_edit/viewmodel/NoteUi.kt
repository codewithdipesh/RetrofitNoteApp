package com.example.notesappretrofit.presentation.add_edit.viewmodel

data class NoteUi(
    var title:String = "",
    val description:String = "",
    val isFavorite:Boolean = false,
    val isLocked:Boolean = false
)
