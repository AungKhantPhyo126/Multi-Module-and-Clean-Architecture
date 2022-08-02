package com.critx.data.datasource.auth

import com.critx.commonkotlin.util.Resource
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.auth.LoginSuccessDto
import kotlinx.coroutines.flow.Flow

interface AuthNetWorkDataSource {
    suspend fun login(username:String,password:String): LoginSuccessDto
    suspend fun logout(token:String):SimpleResponseDto
    suspend fun refreshToken(token:String):LoginSuccessDto
}