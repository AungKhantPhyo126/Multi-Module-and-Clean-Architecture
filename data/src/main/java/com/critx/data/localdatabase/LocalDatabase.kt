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
        sharedPref.edit { putString(TOKEN,token) }
    }

    fun updateToken(updatedToken: String){
        val editor = sharedPref.edit()
        editor.putString(TOKEN,updatedToken)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPref.getString(TOKEN,"")
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