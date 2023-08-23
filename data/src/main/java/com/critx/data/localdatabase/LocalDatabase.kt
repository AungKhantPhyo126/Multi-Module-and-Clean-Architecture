package com.critx.data.localdatabase

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val TOKEN = "token"
private const val REFRESH_TOKEN_EXPIRE = "refresh_token_expire"


@Singleton
class LocalDatabase @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPref = context.getSharedPreferences("shwemi_admin", Context.MODE_PRIVATE)
    fun clearuser(){
        deleteToken()
        deleteRefreshTokenExpireTime()
    }

    fun saveToken(token:String){
        var result = ""
        if (token.startsWith("Bearer")){
            result = token.removePrefix("Bearer ")
        }else{
            result = token
        }
        sharedPref.edit{putString(TOKEN,result)}
    }

    fun updateToken(updatedToken: String){
        val editor = sharedPref.edit()
        editor.putString(TOKEN,updatedToken)
        editor.apply()
    }

    fun getToken(): String? {
        val token =  sharedPref.getString(TOKEN, "0")
        return "Bearer $token"
    }

    fun deleteToken() {
        sharedPref.edit { remove(TOKEN) }
    }

    fun isLogin():Boolean{
        return getToken().isNullOrEmpty().not()
    }


    fun getRefreshTokenExpireTime(): String? {
        return sharedPref.getString(REFRESH_TOKEN_EXPIRE,"")
    }
    fun deleteRefreshTokenExpireTime() {
        sharedPref.edit { remove(REFRESH_TOKEN_EXPIRE) }
    }
}