package com.example.notesappretrofit.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notesappretrofit.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao{

    @Query("select * from NoteEntity")
    fun getAllNotes():Flow<List<NoteEntity>>

    @Insert
    suspend fun createNote(noteEntity: NoteEntity)

    @Update
    suspend fun updateNote(noteEntity: NoteEntity)

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity)

    @Query("select * from NoteEntity where id=:id")
    fun getNoteById(id:Int):Flow<NoteEntity>


}