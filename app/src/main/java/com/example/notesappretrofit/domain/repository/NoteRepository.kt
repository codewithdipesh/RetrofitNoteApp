package com.example.notesappretrofit.domain.repository

import com.example.notesappretrofit.data.remote.note.NoteApi
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val api : NoteApi
){
}