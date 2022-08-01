package com.critx.shwemiAdmin.localDatabase

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

private const val TOKEN = "token"
private const val REFRESH_TOKEN_EXPIRE = "token"


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

    fun getToken(): String? {
        return sharedPref.getString(TOKEN,"")
    }

    fun deleteToken() {
        sharedPref.edit { remove(TOKEN) }
    }

    fun isLogin():Boolean{
        return getToken().isNullOrEmpty().not()
    }

    fun isRefreshTokenExpire():Boolean{
        val today = LocalDateTime.now()
        val expireTime = LocalDateTime.parse(getRefreshTokenExpireTime())
        if (today.isAfter(expireTime)) clearuser()
        return today.isAfter(expireTime)
    }

    fun saveRefreshTokenExpireTime(expireIn: LocalDateTime){
        sharedPref.edit { putString(REFRESH_TOKEN_EXPIRE,expireIn.toString()) }
    }
    fun getRefreshTokenExpireTime(): String? {
        return sharedPref.getString(REFRESH_TOKEN_EXPIRE,"")
    }
    fun deleteRefreshTokenExpireTime() {
        sharedPref.edit { remove(REFRESH_TOKEN_EXPIRE) }
    }
}