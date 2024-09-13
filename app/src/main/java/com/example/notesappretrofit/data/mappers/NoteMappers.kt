package com.example.notesappretrofit.data.mappers

import com.example.notesappretrofit.data.local.entity.NoteEntity
import com.example.notesappretrofit.data.remote.note.dto.NoteDto
import com.example.notesappretrofit.data.remote.note.dto.NoteRequest
import com.example.notesappretrofit.domain.entity.Note

fun NoteDto.toNoteEntity(): NoteEntity{

    return NoteEntity(
        id = id,
        createdAt = createdAt,
        description = description,
        title = title ,
        isLocked = isLocked,
        isFavorite= isFavorite,
        hasSynced = true
    )
}

fun NoteEntity.toNote():Note{
    return Note(
        id = id,
        createdAt = createdAt,
        description = description,
        title = title ,
        isLocked = isLocked,
        isFavorite= isFavorite
    )
}

fun NoteRequest.toNoteEntity():NoteEntity{
    return NoteEntity(
        id = id,
        createdAt = createdAt,
        description = description,
        title = title ,
        isLocked = isLocked,
        isFavorite= isFavorite,
        hasSynced = true
    )
}