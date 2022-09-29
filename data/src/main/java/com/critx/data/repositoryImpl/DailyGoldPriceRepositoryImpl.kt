package com.critx.data.repositoryImpl

import com.critx.commonkotlin.util.Resource
import com.critx.data.GetErrorMessage
import com.critx.data.datasource.dailyGoldAndPrice.DailyGoldAndPriceNetWorkDataSource
import com.critx.data.network.dto.dailygoldAndPrice.asDomain
import com.critx.domain.model.dailyGoldAndPrice.GoldPriceDomain
import com.critx.domain.repository.DailyGoldPriceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DailyGoldPriceRepositoryImpl @Inject constructor(
    private val dailyGoldAndPriceNetWorkDataSource: DailyGoldAndPriceNetWorkDataSource
):DailyGoldPriceRepository {
    override fun getGoldPrice(token: String): Flow<Resource<List<GoldPriceDomain>>>  =
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
}