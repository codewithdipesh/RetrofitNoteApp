package com.example.notesappretrofit.data.repository


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.example.notesappretrofit.data.local.token.TokenManager
import com.example.notesappretrofit.data.remote.user.UserApi
import com.example.notesappretrofit.data.remote.user.dto.UserChangePasswordRequest
import com.example.notesappretrofit.data.remote.user.dto.UserLoginRegisterRequest
import com.example.notesappretrofit.domain.Result
import com.example.notesappretrofit.domain.UserError
import com.example.notesappretrofit.domain.repository.UserRepository
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

import javax.inject.Inject

 class UserRepositoryImpl @Inject constructor(
    private val api : UserApi,
    private val tokenManager: TokenManager
):UserRepository {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun register(request: UserLoginRegisterRequest): Result<Boolean, UserError> {

        return try {
            val result =  api.register(request)
            Log.d("result", result.toString())
            if(result.status != 200){
                //explicitly throw exception
                throw HttpException(Response.error<Any>(result.status, ResponseBody.create(null,"")))
            }
            val token = result.token
            tokenManager.saveToken(token)
            Result.Success(true)

       }catch (e: IOException){
            Log.d("NetworkError", "Network error occurred: ${e.localizedMessage}")
            Result.Error(UserError.NETWORK_ERROR)
       }catch (e : HttpException){
           when(e.code()){
               //Custom Error from Backend
               409 -> Result.Error(UserError.USERNAME_ALREADY_EXIST)
               400 -> Result.Error(UserError.INVALID_CREDENTIAL)
               500 -> Result.Error(UserError.SERVER_ERROR)
               else -> Result.Error(UserError.UNKNOWN_ERROR)
           }
       }catch(e:Exception){
           Result.Error(UserError.UNKNOWN_ERROR)
       }
    }

    override suspend fun login(request: UserLoginRegisterRequest): Result<Boolean, UserError> {
        return try {
            val result =  api.login(request)
            if(result.status != 200){
                //explicitly throw exception
                throw HttpException(Response.error<Any>(result.status, ResponseBody.create(null,"")))
            }
            val token = result.token
            tokenManager.saveToken(token)
            Result.Success(true)

        }catch (e: IOException){
            //No Internet
            Result.Error(UserError.NETWORK_ERROR)
        }catch (e : HttpException){
            when(e.code()){
                //Custom Error from Backend
                400,403 -> Result.Error(UserError.INVALID_CREDENTIAL)
                500 -> Result.Error(UserError.SERVER_ERROR)
                else -> Result.Error(UserError.UNKNOWN_ERROR)
            }
        }catch(e:Exception){
            Result.Error(UserError.UNKNOWN_ERROR)
        }
    }

     override suspend fun changePass(request: UserChangePasswordRequest,token :String): Result<Boolean, UserError> {
         return try {
             val result =  api.changePassword(request,token)
             if(result.status != 200){
                 //explicitly throw exception
                 throw HttpException(Response.error<Any>(result.status, ResponseBody.create(null,"")))
             }
             Result.Success(true)

         }catch (e: IOException){
             //No Internet
             Result.Error(UserError.NETWORK_ERROR)
         }catch (e : HttpException){
             when(e.code()){
                 //Custom Error from Backend
                 400 -> Result.Error(UserError.INVALID_CREDENTIAL)
                 401,403->Result.Error(UserError.UNAUTHORIZED)
                 500 -> Result.Error(UserError.SERVER_ERROR)
                 else -> Result.Error(UserError.UNKNOWN_ERROR)
             }
         }catch(e:Exception){
             Result.Error(UserError.UNKNOWN_ERROR)
         }
     }

     override suspend fun authenticate(token: String): Result<Boolean, UserError> {
         return try{
             val result = api.authenticate(token)
             if(result.status != 200){
                 //explicitly throw exception
                 throw HttpException(Response.error<Any>(result.status, ResponseBody.create(null,"")))
             }
             Result.Success(true)
         }catch (e: IOException){
             //No Internet
             Result.Error(UserError.NETWORK_ERROR)
         }catch (e : HttpException){
             when(e.code()){
                 //Custom Error from Backend
                 401,403->Result.Error(UserError.UNAUTHORIZED)
                 500 -> Result.Error(UserError.SERVER_ERROR)
                 else -> Result.Error(UserError.UNKNOWN_ERROR)
             }
         }catch(e:Exception){
             Result.Error(UserError.UNKNOWN_ERROR)
         }
     }

     override suspend fun getUsername(token: String): Result<String, UserError> {

         return try{
             val result = api.getUsername(token)
             if(result.status != 200){
                 //explicitly throw exception
                 throw HttpException(Response.error<Any>(result.status, ResponseBody.create(null,"")))
             }
             Result.Success(result.token)//the username
         }catch (e: IOException){
             //No Internet
             Result.Error(UserError.NETWORK_ERROR)
         }catch (e : HttpException){
             when(e.code()){
                 //Custom Error from Backend
                 401,403->Result.Error(UserError.UNAUTHORIZED)
                 500 -> Result.Error(UserError.SERVER_ERROR)
                 else -> Result.Error(UserError.UNKNOWN_ERROR)
             }
         }catch(e:Exception){
             Result.Error(UserError.UNKNOWN_ERROR)
         }
     }


 }




