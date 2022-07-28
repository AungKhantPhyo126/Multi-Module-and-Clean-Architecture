package com.critx.shwemiAdmin

import com.critx.data.datasource.auth.AuthNetWorkDataSource
import com.critx.data.network.api.AuthService
import com.critx.data.network.datasource.AuthNetWorkDataSourceImpl
import com.critx.data.repository.AuthRepositoryImpl
import com.critx.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//class AppModule {
//    @Provides
//    @Singleton
//    fun provideAuthRepo(authNetWorkDataSource: AuthNetWorkDataSource):AuthRepository{
//        return AuthRepositoryImpl(authNetWorkDataSource)
//    }
//
//
//}