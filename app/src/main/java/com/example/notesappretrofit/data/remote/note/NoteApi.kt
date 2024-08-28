package com.example.notesappretrofit.data.remote.note

import com.example.notesappretrofit.data.remote.note.dto.GetAllNotesResponse
import com.example.notesappretrofit.data.remote.note.dto.NoteCreateResponse
import com.example.notesappretrofit.data.remote.note.dto.NoteRequest
import com.example.notesappretrofit.data.remote.note.dto.NoteUpdatedDeletedResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteApi {
    @POST("note/")
    suspend fun createNote(
        @Body request : NoteRequest,
        @Header("Authorization") token : String
    ) : NoteCreateResponse

    @GET("note/")
    suspend fun getAllNotes(
        @Header("Authorization") token : String
    ) : GetAllNotesResponse

    @PUT("note/{noteId}")
    suspend fun updateNote(
        @Body request: NoteRequest,
        @Path("noteId") id : String,
        @Header("Authorization") token : String
    ):NoteUpdatedDeletedResponse

    @DELETE("note/{noteId}")
    suspend fun deleteNote(
        @Path("noteId") id : String,
        @Header("Authorization") token : String
    ):NoteUpdatedDeletedResponse

    @GET("note/{noteId}")
    suspend fun getNote(
        @Path("noteId") id : String,
        @Header("Authorization") token : String
    ):NoteCreateResponse

}