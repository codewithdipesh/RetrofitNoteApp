package com.example.notesappretrofit.domain.repository

import com.example.notesappretrofit.data.local.TokenManager
import com.example.notesappretrofit.data.remote.note.NoteApi
import com.example.notesappretrofit.data.remote.note.dto.NoteCreateResponse
import com.example.notesappretrofit.data.remote.note.dto.NoteRequest
import com.example.notesappretrofit.data.remote.user.UserApi
import com.example.notesappretrofit.data.remote.user.dto.AuthenticateResponse
import com.example.notesappretrofit.data.remote.user.dto.UserChangePassResponse
import com.example.notesappretrofit.data.remote.user.dto.UserChangePasswordRequest
import com.example.notesappretrofit.data.remote.user.dto.UserLoginRegisterRequest
import com.example.notesappretrofit.data.remote.user.dto.UserLoginRegisterResponse
import com.example.notesappretrofit.domain.Error
import com.example.notesappretrofit.domain.Result
import com.example.notesappretrofit.domain.UserError
import javax.inject.Inject

interface UserRepository {
    suspend fun register(request: UserLoginRegisterRequest):Result<Boolean,UserError>
    suspend fun login(request: UserLoginRegisterRequest):Result<Boolean,UserError>
    suspend fun changePass(request: UserChangePasswordRequest):Result<Boolean,UserError>
    suspend fun authenticate():Result<Boolean,UserError>
}




