package com.critx.domain.model

data class LogInSuccess(
    val token:String,
    val tokenExpire:Long,
    val refreshTokenExpire:Long
)
