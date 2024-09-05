package com.example.notesappretrofit.data.local.token

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(
    @ApplicationContext context :Context)
{
    private val prefs = context.getSharedPreferences("AppPrefs",Context.MODE_PRIVATE)
    private val TOKEN_KEY = "jwt_token"

    fun saveToken(token:String){
        prefs.edit().putString(TOKEN_KEY,"Bearer $token").apply()
    }
    fun getToken():String?{
        return prefs.getString(TOKEN_KEY,null)
    }
    fun clearToken(){
        prefs.edit().remove(TOKEN_KEY).apply()
    }
}