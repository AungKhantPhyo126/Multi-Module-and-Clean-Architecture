package com.critx.data.repositoryImpl

import com.critx.commonkotlin.util.Resource
import com.critx.data.GetErrorMessage
import com.critx.data.datasource.repairStock.RepairStockDataSource
import com.critx.data.network.dto.asDomain
import com.critx.data.network.dto.repairStock.asDomain
import com.critx.domain.model.SimpleData
import com.critx.domain.model.repairStock.JobDoneDomain
import com.critx.domain.repository.RepairStockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepairStockRepoImpl @Inject constructor(
    private val repairStockDataSource: RepairStockDataSource
):RepairStockRepository {
    override fun getJobDoneData(token: String, goldSmithId: String): Flow<Resource<JobDoneDomain>> =
    flow {
        emit(Resource.Loading())
        try {
            emit(
                Resource.Success(
                    repairStockDataSource.getJobDoneData(token,goldSmithId).asDomain()
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

    override fun assignGoldSmith(
        token: String,
        goldSmithId: RequestBody,
        jewelleryTypeId: RequestBody,
        repairJobId: RequestBody,
        quantity: RequestBody,
        weightGm: RequestBody
    ): Flow<Resource<SimpleData>>  =
    flow {
        emit(Resource.Loading())
        try {
            emit(
                Resource.Success(
                    repairStockDataSource.assignGoldSmith(token,goldSmithId,jewelleryTypeId, repairJobId, quantity, weightGm).asDomain()
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

    override fun chargeRepairSTock(
        token: String,
        goldSmithId: RequestBody,
        repairStockList: List<RequestBody>
    ): Flow<Resource<SimpleData>>  =
    flow {
        emit(Resource.Loading())
        try {
            emit(
                Resource.Success(
                    repairStockDataSource.chargeRepairSTock(token,goldSmithId,repairStockList).asDomain()
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