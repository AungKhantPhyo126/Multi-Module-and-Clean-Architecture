package com.critx.data

import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException

object GetErrorMessage {
    fun fromException(e: IOException): String{
        return   if(e is ConnectException){
            "No internet Connection"
        }else e.message.orEmpty()
    }
    fun fromException(e: HttpException): String{
        return when(e.code()){
            400 -> "Bad request"
            401 -> "You are not authorized"
            402 -> "Payment required!!!"
            403 -> "Forbidden"
            404 -> "You request not found"
            405 -> "Method is not allowed!!!"
            else -> "Unhandled error occurred!!!"
        }
    }
}