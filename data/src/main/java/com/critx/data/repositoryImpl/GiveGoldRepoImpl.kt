package com.critx.data.repositoryImpl

import com.critx.commonkotlin.util.Resource
import com.critx.data.GetErrorMessage
import com.critx.data.datasource.giveGold.GiveGoldDataSource
import com.critx.data.network.dto.asDomain
import com.critx.data.network.dto.giveGold.asDomain
import com.critx.domain.model.SimpleData
import com.critx.domain.model.giveGold.GiveGoldScanDomain
import com.critx.domain.model.giveGold.GoldBoxDomain
import com.critx.domain.repository.GiveGoldRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GiveGoldRepoImpl @Inject constructor(
    private val giveGoldDataSource: GiveGoldDataSource
):GiveGoldRepository {
    override fun giveGold(
        token: String,
        goldSmithId: String,
        orderItem: String,
        orderQty: String,
        weightY: String,
        goldBoxId: String,
        goldWeight: String,
        gemWeight: String,
        goldAndGemWeight:String,
        wastageY: String,
        dueDate: String?,
        sampleList: List<String>?
    ): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        giveGoldDataSource.giveGold(token,goldSmithId, orderItem, orderQty, weightY,
                            goldBoxId, goldWeight, gemWeight,goldAndGemWeight,wastageY, dueDate, sampleList).asDomain()
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

    override fun getGoldBoxId(token: String): Flow<Resource<List<GoldBoxDomain>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        giveGoldDataSource.getGoldBoxId(token).map { it.asDomain() }
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

    override fun serviceCharge(
        token: String,
        chargeAmount: String,
        wastageGm: String,
        invoice: String
    ): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        giveGoldDataSource.serviceCharge(token,chargeAmount,wastageGm, invoice).asDomain()
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

    override fun giveGoldScan(
        token: String,
        invoiceNumber: String
    ): Flow<Resource<GiveGoldScanDomain>>  =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        giveGoldDataSource.giveGoldScan(token,invoiceNumber).asDomain()
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