package com.example.notesappretrofit.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(
    @ApplicationContext context :Context)
{
    private val prefs = context.getSharedPreferences("AppPrefs",Context.MODE_PRIVATE)
    private val TOKEN_KEY = "jwt_token"

    fun saveToken(token:String){
        prefs.edit().putString(TOKEN_KEY,token).apply()
    }
    fun getToken(){
        prefs.getString(TOKEN_KEY,null)
    }
    fun clearToken(){
        prefs.edit().remove(TOKEN_KEY).apply()
    }
}