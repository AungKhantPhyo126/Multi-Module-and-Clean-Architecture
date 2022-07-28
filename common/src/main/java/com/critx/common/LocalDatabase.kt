package com.critx.common

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val TOKEN = "token"


@Singleton
class LocalDatabase @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPref = context.getSharedPreferences("shwemi_admin", Context.MODE_PRIVATE)


    fun saveToken(token:String){
        sharedPref.edit { putString(TOKEN,token) }
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
}