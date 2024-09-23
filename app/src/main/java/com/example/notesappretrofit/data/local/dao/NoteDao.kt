package com.example.notesappretrofit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notesappretrofit.data.local.entity.DeletedNoteEntity
import com.example.notesappretrofit.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao{

    @Query("select * from notes")
    fun getAllNotes():Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNote(noteEntity: NoteEntity)

    @Query("delete from notes where id=:id")
    suspend fun deleteNote(id: Int):Int

    @Query("select * from notes where id=:id")
    fun getNoteById(id:Int):Flow<NoteEntity>

    @Query("select * from notes where hasSynced=0 ")
    fun getUnsyncedNotes():Flow<List<NoteEntity>>

    @Insert
    suspend fun insertDeletedNote(deletedNote: DeletedNoteEntity)

    @Query("SELECT id FROM deleted_notes") // Assuming you have a DeletedNotesEntity
    fun getDeletedNotes(): Flow<List<Int>>

    @Query("DELETE FROM deleted_notes WHERE id = :noteId")
    suspend fun deleteDeletedNote(noteId: Int)


}