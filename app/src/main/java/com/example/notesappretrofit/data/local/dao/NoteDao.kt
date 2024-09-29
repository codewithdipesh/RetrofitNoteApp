package com.example.notesappretrofit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notesappretrofit.data.local.entity.DeletedNoteEntity
import com.example.notesappretrofit.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao{

    @Query("select * from notes")
    fun getAllNotes():Flow<List<NoteEntity>>

    @Insert(entity = NoteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNote(noteEntity: NoteEntity)

    @Update(entity = NoteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(noteEntity: NoteEntity)

    @Insert(entity = NoteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNotes(noteEntity: List<NoteEntity>)

    @Query("delete from notes where id=:id")
    suspend fun deleteNote(id: Int):Int

    @Query("DELETE FROM notes WHERE id IN (:ids)")
    suspend fun deleteNotesByIds(ids: List<Int>)

    @Query("select * from notes where id=:id")
    fun getNoteById(id:Int):Flow<NoteEntity>

    @Query("select * from notes where hasSynced=0 ")
    fun getUnsyncedNotes():Flow<List<NoteEntity>>

    @Query("UPDATE notes SET hasSynced = 1 WHERE id = :id")
    suspend fun markNoteAsSynced(id: Int)

    @Insert(entity = DeletedNoteEntity::class)
    suspend fun insertDeletedNote(deletedNote: DeletedNoteEntity)

    @Query("SELECT id FROM deleted_notes") // Assuming you have a DeletedNotesEntity
    fun getDeletedNotes(): Flow<List<Int>>

    @Query("DELETE FROM deleted_notes WHERE id = :noteId")
    suspend fun deleteDeletedNote(noteId: Int)


}