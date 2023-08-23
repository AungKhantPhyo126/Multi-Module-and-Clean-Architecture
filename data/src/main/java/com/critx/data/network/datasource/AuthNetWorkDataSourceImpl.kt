package com.critx.data.network.datasource

import com.critx.commonkotlin.util.Resource
import com.critx.data.datasource.auth.AuthNetWorkDataSource
import com.critx.data.getErrorMessageFromHashMap
import com.critx.data.localdatabase.LocalDatabase
import com.critx.data.network.api.AuthService
import com.critx.data.network.apiParams.auth.LoginData
import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.auth.LoginSuccessDto
import com.critx.data.network.dto.auth.ProfileDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.AuthError
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.CategoryError
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.SimpleError
import com.critx.data.parseError
import com.critx.data.parseErrorWithDataClass
import javax.inject.Inject

class AuthNetWorkDataSourceImpl @Inject constructor(
    private val authService: AuthService,
    private val localDatabase: LocalDatabase
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
                when (response.code()) {
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                }
            )

        }
    }

    override suspend fun logout(token: String): SimpleResponse {
        val response = authService.logout(token)
        return  if (response.isSuccessful){
            response.body()?:throw Exception("Response body Null")
        }else{
            throw  Exception(
                when (response.code()) {
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                }
            )
        }
    }

    override  fun refreshToken(): Resource<String> {
        return try {
            val responseCall = authService.refreshToken(localDatabase.getToken().orEmpty())
            val response = responseCall.execute()
            if (response.body() != null) {
                localDatabase.saveToken(response.body()!!.data!!.token.orEmpty())
                Resource.Success(response.body()!!.data!!.token)
            } else {
                Resource.Error(response.errorBody()!!.string().toString())
            }
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }

    override suspend fun getProfile(): ProfileDto {
        val response = authService.getProfile(localDatabase.getToken().orEmpty())
        return  if (response.isSuccessful){
            response.body()?:throw Exception("Response body Null")
        }else{
            val errorJsonString = response.errorBody()?.string().orEmpty()
            val errorMessage =
                response.errorBody()?.parseErrorWithDataClass<AuthError>(errorJsonString)?.message
            if (errorMessage.isNullOrEmpty()){
                val errorMessageWithMap =
                    response.errorBody()?.parseError(errorJsonString)
                throw Exception(getErrorMessageFromHashMap(errorMessageWithMap!!))
            }else{
                throw Exception(errorMessage)
            }
        }
    }

}