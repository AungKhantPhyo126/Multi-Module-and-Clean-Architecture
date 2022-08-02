package com.critx.data.di

import com.critx.data.datasource.auth.AuthNetWorkDataSource
import com.critx.data.network.api.AuthService
import com.critx.data.network.api.HomeService
import com.critx.data.network.datasource.AuthNetWorkDataSourceImpl
import com.critx.data.repository.AuthRepositoryImpl
import com.critx.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

const val BASE_URL = "http://18.136.200.98/"

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }
    @Provides
    @Singleton
    fun provideAuthNetWorkDataSource(authService: AuthService):AuthNetWorkDataSource{
        return AuthNetWorkDataSourceImpl(authService)
    }
    @Provides
    @Singleton
    fun provideAuthRepo(authNetWorkDataSource: AuthNetWorkDataSource):AuthRepository{
        return AuthRepositoryImpl(authNetWorkDataSource)
    }
//    @Provides
//    @Singleton
//    fun provideAuthRepository(
//        authNetWorkDataSource: AuthNetWorkDataSource
//    ): AuthRepository{
//        return AuthRepositoryImpl(
//            authNetWorkDataSource,
//        )
//    }


    @Provides
    @Singleton
    fun provideHomeService(retrofit: Retrofit) = retrofit.create<HomeService>()

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit) = retrofit.create<AuthService>()

    @Provides
    @Singleton
    fun provideUncheckOkHttpClient(): OkHttpClient {
        return UnsafeOkHttpClient.getUnsafeOkHttpClient()
    }
}