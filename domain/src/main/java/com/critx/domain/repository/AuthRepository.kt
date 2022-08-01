package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.LogInSuccess
import com.critx.domain.model.SimpleData
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(name:String,password:String): Flow<Resource<LogInSuccess>>
    fun logout(token:String):Flow<Resource<SimpleData>>
}