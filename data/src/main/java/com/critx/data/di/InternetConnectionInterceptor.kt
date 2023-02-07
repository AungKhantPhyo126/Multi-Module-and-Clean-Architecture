package com.critx.data.di

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class InternetConnectionInterceptor @Inject constructor(
    private val connectionObserver: ConnectionObserver
):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var exception:Exception? = null
         with(connectionObserver){
            register()
            onConnected ={
               exception=null
            }
            onDisconnected={
                exception =  Exception("No internet connection")
            }
            onLosing={
                exception = Exception("No internet connection")
            }
            onUnAvailable={
                exception = Exception("No internet connection")
            }
             unregister()
        }
        if (exception != null) {
            throw exception as Exception
        }else{
            return  chain.proceed(chain.request())
        }
    }
}