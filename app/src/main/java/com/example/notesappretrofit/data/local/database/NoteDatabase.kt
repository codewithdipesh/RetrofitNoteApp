package com.example.notesappretrofit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notesappretrofit.data.local.dao.NoteDao
import com.example.notesappretrofit.data.local.entity.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1
)
abstract class NoteDatabase : RoomDatabase() {
    abstract  val dao:NoteDao
}