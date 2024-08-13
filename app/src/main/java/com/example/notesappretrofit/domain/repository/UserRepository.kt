package com.example.notesappretrofit.domain.repository

import com.example.notesappretrofit.data.local.TokenManager
import com.example.notesappretrofit.data.remote.note.NoteApi
import com.example.notesappretrofit.data.remote.note.dto.NoteCreateResponse
import com.example.notesappretrofit.data.remote.note.dto.NoteRequest
import com.example.notesappretrofit.data.remote.user.UserApi
import com.example.notesappretrofit.domain.Error
import com.example.notesappretrofit.domain.Result
import javax.inject.Inject

interface UserRepository {
    suspend fun register(noteRequest: NoteRequest):Result<NoteCreateResponse,>
}




