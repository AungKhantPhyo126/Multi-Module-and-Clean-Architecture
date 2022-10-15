package com.critx.data.repositoryImpl

import com.critx.commonkotlin.util.Resource
import com.critx.data.GetErrorMessage
import com.critx.data.datasource.transferCheckUp.TransferCheckUpNetWorkDataSource
import com.critx.data.network.dto.asDomain
import com.critx.data.network.dto.transferCheckUp.asDomain
import com.critx.domain.model.SimpleData
import com.critx.domain.model.transferCheckUP.CheckUpDomain
import com.critx.domain.repository.TransferCheckUpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TransferCheckUpRepositoryImpl @Inject constructor(
    private val transferCheckUpNetWorkDataSource: TransferCheckUpNetWorkDataSource
) :TransferCheckUpRepository{
    override fun checkUp(
        token: String,
        boxCode: String,
        productIdList: List<String>
    ): Flow<Resource<CheckUpDomain>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        transferCheckUpNetWorkDataSource.checkUp(token,boxCode,productIdList).asDomain()
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


    override fun transfer(
        token: String,
        boxCode: String,
        productIdList: List<String>,
        rfidCode: HashMap<String, String>
    ): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        transferCheckUpNetWorkDataSource.transfer(token,boxCode,productIdList,rfidCode).asDomain()
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