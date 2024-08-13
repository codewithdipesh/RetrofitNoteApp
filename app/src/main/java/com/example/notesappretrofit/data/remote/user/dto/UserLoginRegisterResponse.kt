package com.example.notesappretrofit.data.remote.user.dto

import com.google.gson.annotations.SerializedName

data class UserLoginRegisterResponse(
    @SerializedName("data")
    val token: String,
    val message: String,
    val status: Int
)