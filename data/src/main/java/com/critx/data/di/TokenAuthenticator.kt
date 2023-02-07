package com.critx.data.di

import android.util.Log
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.data.network.datasource.AuthNetWorkDataSourceImpl
import dagger.Lazy
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val authNetWorkDataSourceImpl: Lazy<AuthNetWorkDataSourceImpl>,
    private val localDatabase: LocalDatabase
) : Authenticator {

    var result: Request? = null
    private fun getNewAccessToken(): Resource<String> {
        return authNetWorkDataSourceImpl.get().refreshToken()
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.body?.contentLength() == 103L) {
            return null
        } else {
            val refreshToken = getNewAccessToken()
            when(refreshToken){
                is Resource.Loading->{

                }
                is Resource.Success->{
                    localDatabase.saveToken(refreshToken.data.orEmpty())
                    result = response.request.newBuilder()
                        .header("Authorization", localDatabase.getToken().orEmpty())
                        .build().also {
                            Log.i("new_request", "new req url => ${it.url}, new req header size => ${it.headers.size}, new token => ${refreshToken.data}")
                        }
                }
                is Resource.Error->{
                    result = null
                }
            }
            return result

        }
    }

}
