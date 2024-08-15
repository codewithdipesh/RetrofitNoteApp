package com.example.notesappretrofit.utils

import com.example.notesappretrofit.domain.UserError

public fun mapUserErrorToMessage(error: UserError): String {
    return when (error) {
        UserError.NETWORK_ERROR -> "Please check your internet connection"
        UserError.USERNAME_ALREADY_EXIST -> "Username already exists"
        UserError.INVALID_CREDENTIAL -> "Invalid username or password."
        UserError.SERVER_ERROR -> "Server error. Please try again later."
        UserError.UNAUTHORIZED -> "Unauthorized. Please log in again."
        UserError.UNKNOWN_ERROR -> "An unknown error occurred. Please try again."
    }
}