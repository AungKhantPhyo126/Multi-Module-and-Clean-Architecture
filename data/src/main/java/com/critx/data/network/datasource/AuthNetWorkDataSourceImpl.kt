package com.critx.data.network.datasource

import com.critx.commonkotlin.util.Resource
import com.critx.data.datasource.auth.AuthNetWorkDataSource
import com.critx.data.network.api.AuthService
import com.critx.data.network.apiParams.auth.LoginData
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.auth.LoginSuccessDto
import com.critx.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthNetWorkDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthNetWorkDataSource {
    override suspend fun login(
        username: String,
        password: String
    ): LoginSuccessDto {
        val response = authService.login(LoginData(username, password))
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when(response.code()){
                    400 -> "Bad request"
                    401 -> "Username or Password wrong"
                    402 -> "Payment required!!!"
                    403 -> "Forbidden"
                    404 -> "You request not found"
                    405 -> "Method is not allowed!!!"
                    else -> "Unhandled error occurred!!!"
                }
            )

        }
    }

    override suspend fun logout(token: String): SimpleResponseDto {
        val response = authService.logout(token)
        return  if (response.isSuccessful){
            response.body()?:throw Exception("Response body Null")
        }else{
            throw  Exception(
                when(response.code()){
                    400 -> "Bad request"
                    401 -> "You are not Authorized"
                    402 -> "Payment required!!!"
                    403 -> "Forbidden"
                    404 -> "You request not found"
                    405 -> "Method is not allowed!!!"
                    else -> "Unhandled error occurred!!!"
                }
            )
        }
    }

    override suspend fun refreshToken(token: String): LoginSuccessDto {
        val response = authService.refreshToken(token)
        return  if (response.isSuccessful){
            response.body()?:throw Exception("Response body Null")
        }else{
            throw  Exception(
                when(response.code()){
                    400 -> "Bad request"
                    401 -> "You are not Authorized"
                    402 -> "Payment required!!!"
                    403 -> "Forbidden"
                    404 -> "You request not found"
                    405 -> "Method is not allowed!!!"
                    else -> "Unhandled error occurred!!!"
                }
            )
        }
    }

}