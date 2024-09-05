package com.example.notesappretrofit.domain.repository

import com.example.notesappretrofit.data.remote.user.dto.UserChangePasswordRequest
import com.example.notesappretrofit.data.remote.user.dto.UserLoginRegisterRequest
import com.example.notesappretrofit.domain.Result
import com.example.notesappretrofit.domain.UserError

interface UserRepository {
    suspend fun register(request: UserLoginRegisterRequest):Result<Boolean,UserError>
    suspend fun login(request: UserLoginRegisterRequest):Result<Boolean,UserError>
    suspend fun changePass(request: UserChangePasswordRequest,token :String):Result<Boolean,UserError>
    suspend fun authenticate(token: String):Result<Boolean,UserError>
    suspend fun getUsername(token: String):Result<String,UserError>
}




