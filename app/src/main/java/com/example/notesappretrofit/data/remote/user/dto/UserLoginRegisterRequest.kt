package com.example.notesappretrofit.data.remote.user.dto

data class UserLoginRegisterRequest(
    val password: String,
    val username: String
)