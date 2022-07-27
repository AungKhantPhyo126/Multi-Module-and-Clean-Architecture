package com.critx.data.network.dto.auth

import com.critx.data.network.dto.SimpleResponseDto
import com.critx.domain.model.LogInSuccess

data class LoginSuccessDto(
    val response:SimpleResponseDto?,
    val data:LoginSuccessResponse?
)

data class LoginSuccessResponse(
    val token:String?,
    val token_type:String?,
    val access_token_expires_in:Long?,
    val refresh_token_expires_in:Long?
)

fun LoginSuccessDto.asDomain():LogInSuccess{
    return LogInSuccess(
        token = data?.token_type+" "+data?.token,
        tokenExpire = data?.access_token_expires_in?:0,
        refreshTokenExpire = data?.refresh_token_expires_in?:0
    )
}