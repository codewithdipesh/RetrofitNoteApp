package com.example.notesappretrofit.utils

import com.example.notesappretrofit.domain.NoteError
import com.example.notesappretrofit.domain.UserError

public fun mapNoteErrorToMessage(error: NoteError): String {
    return when (error) {
        NoteError.NETWORK_ERROR -> "Cant reach server"
        NoteError.NOTE_NOT_FOUND -> "Cant found"
        NoteError.INVALID_INPUT -> "Invalid Input"
        NoteError.UNAUTHORIZED -> "Unauthorized"
        NoteError.SERVER_ERROR -> "cant reach erver"
        NoteError.UNKNOWN_ERROR -> "Something went wrong"
    }
}