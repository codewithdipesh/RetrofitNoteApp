package com.example.notesappretrofit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notesappretrofit.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao{

    @Query("select * from NoteEntity")
    fun getAllNotes():Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNote(noteEntity: NoteEntity)

    @Query("delete from NoteEntity where id=:id")
    suspend fun deleteNote(id: Int)

    @Query("select * from NoteEntity where id=:id")
    fun getNoteById(id:Int):Flow<NoteEntity>




}