package com.critx.data.di

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class UnauthorizedInterceptor @Inject constructor(
    private val tokenAuthenticator: TokenAuthenticator
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

//        if (response.code == 401) {
            // Customize the response body here
            val customResponse = response.newBuilder()
                .code(401)
                .message("TOKEN_EXPIRED")
                .build()
//            tokenAuthenticator.authenticate(null,response)
            return customResponse
//        }

    }
}
