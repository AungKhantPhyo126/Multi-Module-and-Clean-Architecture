package com.critx.data.network.api

import com.critx.data.network.apiParams.auth.LoginData
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.auth.LoginSuccessDto
import com.critx.data.network.dto.auth.ProfileDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("api/auth/login")
    suspend fun login(
        @Body loginData: LoginData
    ): Response<LoginSuccessDto>

    @POST("api/auth/logout")
    suspend fun logout(
        @Header("Authorization") token:String
    ):Response<SimpleResponseDto>

    @GET("api/auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") token:String
    ):Response<LoginSuccessDto>


    @GET("api/profile")
    suspend fun getProfile(
        @Header("Authorization") token:String
    ):Response<ProfileDto>
}