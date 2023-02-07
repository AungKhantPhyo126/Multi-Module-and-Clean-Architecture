package com.critx.shwemiAdmin.di

import com.critx.shwemiAdmin.ConnectionObserver
import com.critx.shwemiAdmin.ConnectionObserverImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindConnectionObserver(
        connectionObserver: ConnectionObserverImpl
    ): ConnectionObserver

}