package com.example.notesappretrofit.data.repository


import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.notesappretrofit.data.local.TokenManager
import com.example.notesappretrofit.data.remote.note.NoteApi
import com.example.notesappretrofit.data.remote.note.dto.GetAllNotesResponse
import com.example.notesappretrofit.data.remote.note.dto.NoteCreateResponse
import com.example.notesappretrofit.data.remote.note.dto.NoteData
import com.example.notesappretrofit.data.remote.note.dto.NoteRequest
import com.example.notesappretrofit.data.remote.note.dto.NoteUpdatedDeletedResponse
import com.example.notesappretrofit.data.remote.user.UserApi
import com.example.notesappretrofit.data.remote.user.dto.UserChangePassResponse
import com.example.notesappretrofit.data.remote.user.dto.UserChangePasswordRequest
import com.example.notesappretrofit.data.remote.user.dto.UserLoginRegisterRequest
import com.example.notesappretrofit.data.remote.user.dto.UserLoginRegisterResponse
import com.example.notesappretrofit.domain.NoteError
import com.example.notesappretrofit.domain.Result
import com.example.notesappretrofit.domain.UserError
import com.example.notesappretrofit.domain.repository.NoteRepository
import com.example.notesappretrofit.domain.repository.UserRepository
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

import javax.inject.Inject

 class NoteRepositoryImpl @Inject constructor(
    private val api : NoteApi
):NoteRepository {
     override suspend fun createNote(
         request: NoteRequest,
         token: String
     ): Result<NoteData, NoteError> {
         return try {
             val result = api.createNote(request, token)
             if(result.status != 200) {
                 //explicitly throw exception
                 throw HttpException(Response.error<Any>(result.status, ResponseBody.create(null, "" )))
             }
             Result.Success(result.noteDetails)
         } catch (e: IOException) {
             //No Internet
             Result.Error(NoteError.NETWORK_ERROR)
         } catch (e: HttpException) {
             when (e.code()) {
                 //Custom Error from Backend
                 401,403-> Result.Error(NoteError.UNAUTHORIZED)
                 400 -> Result.Error(NoteError.INVALID_INPUT)
                 500 -> Result.Error(NoteError.SERVER_ERROR)
                 else -> Result.Error(NoteError.UNKNOWN_ERROR)
             }
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     override suspend fun updateNote(
         request: NoteRequest,
         noteId: String,
         token: String
     ): Result<Boolean, NoteError> {
         return try {
            val result = api.updateNote(request,noteId,token)
            if(result.status != 200) {
                throw HttpException(Response.error<Any>(result.status, ResponseBody.create(null,"")))
            }
            Result.Success(result.Isupdated)
         } catch (e: IOException) {
             //No Internet
             Result.Error(NoteError.NETWORK_ERROR)
         } catch (e: HttpException) {
             when (e.code()) {
                 //Custom Error from Backend
                 401,403-> Result.Error(NoteError.UNAUTHORIZED)
                 400 -> Result.Error(NoteError.INVALID_INPUT)
                 500 -> Result.Error(NoteError.SERVER_ERROR)
                 else -> Result.Error(NoteError.UNKNOWN_ERROR)
             }
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     override suspend fun deleteNote(noteId: String, token: String): Result<Boolean, NoteError> {
         return try {
             val result = api.deleteNote(noteId,token)
             if(result.status != 200) {
                 //explicitly throw exception
                 throw HttpException(Response.error<Any>(result.status, ResponseBody.create(null, "" )))
             }
             Result.Success(result.Isupdated)
         } catch (e: IOException) {
             //No Internet
             Result.Error(NoteError.NETWORK_ERROR)
         } catch (e: HttpException) {
             when (e.code()) {
                 //Custom Error from Backend
                 401,403-> Result.Error(NoteError.UNAUTHORIZED)
                 400 -> Result.Error(NoteError.NOTE_NOT_FOUND)
                 500 -> Result.Error(NoteError.SERVER_ERROR)
                 else -> Result.Error(NoteError.UNKNOWN_ERROR)
             }
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     override suspend fun getAllNotes(token: String): Result<List<NoteData>, NoteError> {
         return try {
             val result = api.getAllNotes(token)
             if(result.status != 200) {
                 //explicitly throw exception
                 throw HttpException(Response.error<Any>(result.status, ResponseBody.create(null, "" )))
             }
             Result.Success(result.notes)
         } catch (e: IOException) {
             //No Internet
             Result.Error(NoteError.NETWORK_ERROR)
         } catch (e: HttpException) {
             when (e.code()) {
                 //Custom Error from Backend
                 401,403-> Result.Error(NoteError.UNAUTHORIZED)
                 400 -> Result.Error(NoteError.INVALID_INPUT)
                 500 -> Result.Error(NoteError.SERVER_ERROR)
                 else -> Result.Error(NoteError.UNKNOWN_ERROR)
             }
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

     override suspend fun getNoteById(token: String, id: String): Result<NoteData, NoteError> {
         return try {
             val result = api.getNote(id,token)
             if(result.status != 200) {
                 //explicitly throw exception
                 throw HttpException(Response.error<Any>(result.status, ResponseBody.create(null, "" )))
             }
             Result.Success(result.noteDetails)
         } catch (e: IOException) {
             //No Internet
             Result.Error(NoteError.NETWORK_ERROR)
         } catch (e: HttpException) {
             when (e.code()) {
                 //Custom Error from Backend
                 401,403-> Result.Error(NoteError.UNAUTHORIZED)
                 400 -> Result.Error(NoteError.INVALID_INPUT)
                 500 -> Result.Error(NoteError.SERVER_ERROR)
                 else -> Result.Error(NoteError.UNKNOWN_ERROR)
             }
         } catch (e: Exception) {
             Result.Error(NoteError.UNKNOWN_ERROR)
         }
     }

 }




