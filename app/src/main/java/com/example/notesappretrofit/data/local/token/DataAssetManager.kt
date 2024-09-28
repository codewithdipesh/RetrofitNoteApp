package com.example.notesappretrofit.data.local.token

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DataAssetManager @Inject constructor(
    @ApplicationContext context :Context)
{
    private val prefs = context.getSharedPreferences("AppPrefs",Context.MODE_PRIVATE)
    private val TOKEN_KEY = "jwt_token"
    private  val TEMP_ID_COUNTER_KEY = "temp_id_counter"
    private val USERNAME = "_username_"



    fun saveToken(token:String){
        prefs.edit().putString(TOKEN_KEY,"Bearer $token").apply()
    }
    fun getToken():String?{
        return prefs.getString(TOKEN_KEY,null)
    }
    fun clearToken(){
        prefs.edit().remove(TOKEN_KEY).apply()
    }


    fun saveUsername(username:String){
        prefs.edit().putString(USERNAME,username).apply()
    }
    fun getUsername():String?{
        return prefs.getString(USERNAME,null)
    }
    fun clearUsername(){
        prefs.edit().remove(USERNAME).apply()
    }

    private fun savetempCounter(){
        prefs.edit().putString(TEMP_ID_COUNTER_KEY,(-1).toString()).apply()
    }
    private fun decreseCounter(){
        val temp = prefs.getString(TEMP_ID_COUNTER_KEY,null)
        prefs.edit().putString(TEMP_ID_COUNTER_KEY,(temp!!.toInt()-1).toString()).apply()
    }
    fun gettempCounter():String?{
        val temp = prefs.getString(TEMP_ID_COUNTER_KEY,null)
        if(temp == null){
            savetempCounter()
            return prefs.getString(TEMP_ID_COUNTER_KEY,(-1).toString())
        }
        decreseCounter()
        return prefs.getString(TEMP_ID_COUNTER_KEY,(-1).toString())
    }
    fun cleartempCounter(){
        prefs.edit().remove(USERNAME).apply()
    }



}