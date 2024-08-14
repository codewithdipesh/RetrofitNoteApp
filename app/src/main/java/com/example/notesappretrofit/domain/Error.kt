package com.example.notesappretrofit.domain

sealed interface Error

enum class UserError : Error {
    INVALID_CREDENTIAL,
    USERNAME_ALREADY_EXIST,
    UNAUTHORIZED,
    SERVER_ERROR,
    NETWORK_ERROR,
    UNKNOWN_ERROR
}
enum class NoteError : Error {
    NOTE_NOT_FOUND,
    INVALID_INPUT,
    UNAUTHORIZED,
    SERVER_ERROR,
    NETWORK_ERROR,
    UNKNOWN_ERROR
}
