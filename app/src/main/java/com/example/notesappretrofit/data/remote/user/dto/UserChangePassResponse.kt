package com.example.notesappretrofit.data.remote.user.dto

import com.google.gson.annotations.SerializedName

data class UserChangePassResponse(
    @SerializedName("data")
    val isChanged: Boolean,
    val message: String,
    val status: Int
)