package com.critx.data.repositoryImpl

import com.critx.commonkotlin.util.Resource
import com.critx.data.GetErrorMessage
import com.critx.data.datasource.SampleTakeAndReturn.SampleTakeAndReturnNetWorkDataSource
import com.critx.data.network.dto.asDomain
import com.critx.data.network.dto.sampleTakeAndReturn.asDomain
import com.critx.domain.model.SimpleData
import com.critx.domain.model.sampleTakeAndReturn.HandedListDomain
import com.critx.domain.model.sampleTakeAndReturn.SampleCheckDomain
import com.critx.domain.model.sampleTakeAndReturn.VoucherSampleDomain
import com.critx.domain.model.sampleTakeAndReturn.VoucherScanDomain
import com.critx.domain.repository.SampleTakeAndReturnRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SampleTakeAndReturnRepositoryImpl @Inject constructor(
    private val sampleTakeAndReturnNetWorkDataSource: SampleTakeAndReturnNetWorkDataSource
):SampleTakeAndReturnRepository {
    override fun scanInvoice(
        token: String,
        invoiceCode: String
    ): Flow<Resource<VoucherScanDomain>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        sampleTakeAndReturnNetWorkDataSource.scanInvoice(token, invoiceCode).asDomain()
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


    override fun getVoucherSample(
        token: String,
        invoiceId: String
    ): Flow<Resource<List<VoucherSampleDomain>>>  =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        sampleTakeAndReturnNetWorkDataSource.getVoucherSample(token, invoiceId).map { it.asDomain() }
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

    override fun getOutsideSample(token: String): Flow<Resource<List<VoucherSampleDomain>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        sampleTakeAndReturnNetWorkDataSource.getOutsideSample(token).map { it.asDomain() }
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

    override fun checkSample(
        token: String,
        invoiceId: String
    ): Flow<Resource<List<SampleCheckDomain>>> =
    flow {
        emit(Resource.Loading())
        try {
            emit(
                Resource.Success(
                    sampleTakeAndReturnNetWorkDataSource.checkSample(token, invoiceId).map { it.asDomain() }
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
    override fun saveSample(
        token: String,
        sample: HashMap<String, String>
    ): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        sampleTakeAndReturnNetWorkDataSource.saveSample(token, sample).asDomain()
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

    override fun getHandedList(token: String): Flow<Resource<List<HandedListDomain>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        sampleTakeAndReturnNetWorkDataSource.getHandedList(token).map { it.asDomain() }
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

    override fun addToHandedList(
        token: String,
        sampleId: List<String>
    ): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        sampleTakeAndReturnNetWorkDataSource.addToHandedList(token, sampleId).asDomain()
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

    override fun removeFromHandedList(
        token: String,
        sampleId: List<String>
    ): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        sampleTakeAndReturnNetWorkDataSource.removeFromHandedList(token, sampleId).asDomain()
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

    override fun saveOutsideSample(
        token: String,
        name: RequestBody?,
        weight: RequestBody?,
        specification: RequestBody?,
        image: MultipartBody.Part
    ): Flow<Resource<SimpleData>> =
    flow {
        emit(Resource.Loading())
        try {
            emit(
                Resource.Success(
                    sampleTakeAndReturnNetWorkDataSource.saveOutsideSample(token, name,weight, specification, image).asDomain()
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

    override fun returnSample(token: String, sampleId: List<String>): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        sampleTakeAndReturnNetWorkDataSource.returnSample(token, sampleId).asDomain()
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