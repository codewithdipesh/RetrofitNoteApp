package com.example.notesappretrofit.domain

sealed interface Error

enum class UserError : Error {
    VALID_CREDENTIAL,
    USERNAME_ALREADY_EXIST,
    UNAUTHORIZED,
    SERVER_ERROR
}
enum class NoteError : Error {
    NOTE_NOT_FOUND,
    UNAUTHORIZED,
    SERVER_ERROR
}
