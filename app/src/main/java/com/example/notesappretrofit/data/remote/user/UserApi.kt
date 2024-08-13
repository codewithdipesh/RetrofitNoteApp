package com.example.notesappretrofit.data.remote.user

import com.example.notesappretrofit.data.remote.user.dto.UserChangePassResponse
import com.example.notesappretrofit.data.remote.user.dto.UserChangePasswordRequest
import com.example.notesappretrofit.data.remote.user.dto.UserLoginRegisterRequest
import com.example.notesappretrofit.data.remote.user.dto.UserLoginRegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("/public/create-new")
    suspend fun register(
        @Body request : UserLoginRegisterRequest
    ): UserLoginRegisterResponse
    @POST("/public/login")
    suspend fun login(
        @Body request : UserLoginRegisterRequest
    ): UserLoginRegisterResponse
    @POST("/user/change-password")
    suspend fun  changePassword(
        @Body request : UserChangePasswordRequest
    ): UserChangePassResponse
//    @POST("/public/create-new")
//    suspend fun deleteUser(
//        @Body request :
//    ): UserLoginRegisterResponse




}