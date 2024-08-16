package com.example.notesappretrofit.data.remote.user.dto

import com.google.gson.annotations.SerializedName

data class AuthenticateResponse(
    @SerializedName("data")
    val isAuthorized: Boolean,
    val message: String,
    val status: Int
)
