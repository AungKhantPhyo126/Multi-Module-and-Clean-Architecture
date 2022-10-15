package com.critx.data.di

import com.critx.data.datasource.SampleTakeAndReturn.SampleTakeAndReturnNetWorkDataSource
import com.critx.data.datasource.auth.AuthNetWorkDataSource
import com.critx.data.datasource.collectStock.CollectStockDataSource
import com.critx.data.datasource.dailyGoldAndPrice.DailyGoldAndPriceNetWorkDataSource
import com.critx.data.datasource.setupstock.SetupStockNetWorkDatasource
import com.critx.data.network.api.*
import com.critx.data.network.datasource.AuthNetWorkDataSourceImpl
import com.critx.data.network.datasource.CollectStockDataSourceImpl
import com.critx.data.network.datasource.DailyGoldAndPriceDataSourceImpl
import com.critx.data.network.datasource.SampleTakeAndReturnDataSourceImpl
import com.critx.data.network.datasource.SetupStockNetWorkSourceImpl
import com.critx.data.repositoryImpl.AuthRepositoryImpl
import com.critx.data.repositoryImpl.CollectStockRepositoryImpl
import com.critx.data.repositoryImpl.DailyGoldPriceRepositoryImpl
import com.critx.data.repositoryImpl.SampleTakeAndReturnRepositoryImpl
import com.critx.data.repositoryImpl.SetupStockRepositoryImpl
import com.critx.domain.repository.AuthRepository
import com.critx.domain.repository.CollectStockRepository
import com.critx.domain.repository.DailyGoldPriceRepository
import com.critx.domain.repository.SampleTakeAndReturnRepository
import com.critx.domain.repository.SetupStockRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

const val BASE_URL = "http://13.214.194.201/"

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
    fun provideAuthNetWorkDataSource(authService: AuthService): AuthNetWorkDataSource {
        return AuthNetWorkDataSourceImpl(authService)
    }

    @Provides
    @Singleton
    fun provideAuthRepo(authNetWorkDataSource: AuthNetWorkDataSource): AuthRepository {
        return AuthRepositoryImpl(authNetWorkDataSource)
    }

    @Provides
    @Singleton
    fun provideSetupStockNetWorkDatasource(setupStockService: SetUpStockService): SetupStockNetWorkDatasource {
        return SetupStockNetWorkSourceImpl(setupStockService)
    }

    @Provides
    @Singleton
    fun provideSetupStockRepo(setupStockNetWorkDatasource: SetupStockNetWorkDatasource): SetupStockRepository {
        return SetupStockRepositoryImpl(setupStockNetWorkDatasource)
    }

    @Provides
    @Singleton
    fun provideCollectStockDataSource(collectStockService: CollectStockService): CollectStockDataSource {
        return CollectStockDataSourceImpl(collectStockService)
    }

    @Provides
    @Singleton
    fun provideCollectStockRepo(collectStockDataSource: CollectStockDataSource): CollectStockRepository {
        return CollectStockRepositoryImpl(collectStockDataSource)
    }

    @Provides
    @Singleton
    fun provideDailyGoldPriceDataSource(dailyGoldPriceService: DailyGoldPriceService):DailyGoldAndPriceNetWorkDataSource{
        return DailyGoldAndPriceDataSourceImpl(dailyGoldPriceService)
    }
    @Provides
    @Singleton
    fun provideDailyGoldPrice(dailyGoldAndPriceNetWorkDataSource: DailyGoldAndPriceNetWorkDataSource):DailyGoldPriceRepository{
        return DailyGoldPriceRepositoryImpl(dailyGoldAndPriceNetWorkDataSource)
    }

    @Provides
    @Singleton
    fun provideSampleTakeAndReturnDataSource(sampleTakeAndReturnService: SampleTakeAndReturnService): SampleTakeAndReturnNetWorkDataSource {
        return SampleTakeAndReturnDataSourceImpl(sampleTakeAndReturnService)
    }

    @Provides
    @Singleton
    fun provideSampleTakeAndReturnRepo(sampleTakeAndReturnNetWorkDataSource: SampleTakeAndReturnNetWorkDataSource): SampleTakeAndReturnRepository {
        return SampleTakeAndReturnRepositoryImpl(sampleTakeAndReturnNetWorkDataSource)
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
    fun provideDailyGoldPriceService(retrofit: Retrofit) = retrofit.create<DailyGoldPriceService>()
    @Provides
    @Singleton
    fun provideSampleTakeAndReturnService(retrofit: Retrofit) =
        retrofit.create<SampleTakeAndReturnService>()

    @Provides
    @Singleton
    fun provideHomeService(retrofit: Retrofit) = retrofit.create<HomeService>()

    @Provides
    @Singleton
    fun provideCollectStockService(retrofit: Retrofit) = retrofit.create<CollectStockService>()

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit) = retrofit.create<AuthService>()

    @Provides
    @Singleton
    fun provideSetUpStockService(retrofit: Retrofit) = retrofit.create<SetUpStockService>()

    @Provides
    @Singleton
    fun provideUncheckOkHttpClient(): OkHttpClient {
        return UnsafeOkHttpClient.getUnsafeOkHttpClient()
    }
}