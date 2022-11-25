package com.critx.data.repositoryImpl

import com.critx.commonkotlin.util.Resource
import com.critx.data.GetErrorMessage
import com.critx.data.datasource.dailyGoldAndPrice.DailyGoldAndPriceNetWorkDataSource
import com.critx.data.network.dto.asDomain
import com.critx.data.network.dto.dailygoldAndPrice.asDomain
import com.critx.domain.model.SimpleData
import com.critx.domain.model.dailyGoldAndPrice.GoldPriceDomain
import com.critx.domain.model.dailyGoldAndPrice.RebuyPriceSmallAndLargeDomain
import com.critx.domain.repository.DailyGoldPriceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DailyGoldPriceRepositoryImpl @Inject constructor(
    private val dailyGoldAndPriceNetWorkDataSource: DailyGoldAndPriceNetWorkDataSource
) : DailyGoldPriceRepository {
    override fun getGoldPrice(token: String): Flow<Resource<List<GoldPriceDomain>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        dailyGoldAndPriceNetWorkDataSource.getGoldPrice(token).map { it.asDomain() }
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unhandled Error"))
            }
        }

    override fun updateGoldPrice(
        token: String,
        price: HashMap<String, String>
    ): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        dailyGoldAndPriceNetWorkDataSource.updateGoldPrice(token, price).asDomain()
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unhandled Error"))
            }
        }

    override fun getRebuyPrice(token: String): Flow<Resource<RebuyPriceSmallAndLargeDomain>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        dailyGoldAndPriceNetWorkDataSource.getRebuyPrice(token).asDomain()
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unhandled Error"))
            }
        }


    override fun updateRebuyPrice(
        token: String,
        horizontal_option_name: HashMap<String, String>,
        vertical_option_name: HashMap<String, String>,
        horizontal_option_level: HashMap<String, String>,
        vertical_option_level: HashMap<String, String>,
        size: HashMap<String, String>,
        price: HashMap<String, String>
    ): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        dailyGoldAndPriceNetWorkDataSource.updateRebuyPrice(
                            token,
                            horizontal_option_name,
                            vertical_option_name,
                            horizontal_option_level,
                            vertical_option_level,
                            size,
                            price
                        ).asDomain()
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unhandled Error"))
            }
        }
}