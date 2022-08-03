package com.critx.data.repository

import com.critx.commonkotlin.util.Resource
import com.critx.data.GetErrorMessage
import com.critx.data.datasource.setupstock.SetupStockNetWorkDatasource
import com.critx.data.network.dto.auth.asDomain
import com.critx.data.network.dto.setupStock.jewelleryQuality.asDomain
import com.critx.data.network.dto.setupStock.jewelleryType.asDomain
import com.critx.domain.model.SetupStock.JewelleryType.JewelleryType
import com.critx.domain.model.SetupStock.jewelleryQuality.JewelleryQuality
import com.critx.domain.repository.SetupStockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SetupStockRepositoryImpl @Inject constructor(
    private val setupStockNetWorkDatasource: SetupStockNetWorkDatasource
):SetupStockRepository {
    override fun getJewelleryType(token: String): Flow<Resource<List<JewelleryType>>> =
    flow {
        emit(Resource.Loading())
        try {
            emit(
                Resource.Success(
                    setupStockNetWorkDatasource.getJewelleryType(token).data.map { it.asDomain() }
                )
            )
        } catch (e: HttpException) {
            emit(Resource.Error(GetErrorMessage.fromException(e)))
        } catch (e: IOException) {
            emit(Resource.Error(GetErrorMessage.fromException(e)))
        }catch (e: Exception) {
            emit(Resource.Error(e.message?:"Unhandled Error"))
        }
    }

    override fun getJewelleryQuality(token: String): Flow<Resource<List<JewelleryQuality>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        setupStockNetWorkDatasource.getJewelleryQuality(token).map { it.asDomain() }
                    )
                )
            } catch (e: HttpException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            } catch (e: IOException) {
                emit(Resource.Error(GetErrorMessage.fromException(e)))
            }catch (e: Exception) {
                emit(Resource.Error(e.message?:"Unhandled Error"))
            }
        }
}