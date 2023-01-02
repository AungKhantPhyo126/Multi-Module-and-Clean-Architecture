package com.critx.data.repositoryImpl

import com.critx.commonkotlin.util.Resource
import com.critx.data.GetErrorMessage
import com.critx.data.datasource.collectStock.CollectStockDataSource
import com.critx.data.network.dto.asDomain
import com.critx.data.network.dto.collectStock.asDomain
import com.critx.data.network.dto.setupStock.jewelleryType.asDomain
import com.critx.domain.model.SimpleData
import com.critx.domain.model.collectStock.GoldSmithListDomain
import com.critx.domain.model.collectStock.JewellerySizeDomain
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
import com.critx.domain.repository.CollectStockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CollectStockRepositoryImpl @Inject constructor(
    private val collectStockDataSource: CollectStockDataSource
) : CollectStockRepository {
    override fun getProductId(token: String, productCode: String): Flow<Resource<String>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        collectStockDataSource.getProductId(token, productCode)
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

    override fun collectSingle(
        token: String,
        productCode: String,
        weight: RequestBody
    ): Flow<Resource<String>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        collectStockDataSource.collectSingle(token, productCode, weight)
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

    override fun scanProductCode(
        token: String,
        productCode: String
    ): Flow<Resource<ProductIdWithTypeDomain>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        collectStockDataSource.scanProductCode(token, productCode).asDomain()
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

    override fun getJewellerySize(
        token: String,
        type: String
    ): Flow<Resource<List<JewellerySizeDomain>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        collectStockDataSource.getJewellerySize(token, type).map { it.asDomain() }
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

    override fun getGoldSmithList(token: String,type: String): Flow<Resource<List<GoldSmithListDomain>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        collectStockDataSource.getGoldSmithList(token,type).map { it.asDomain() }
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

    override  fun collectBatch(
        token: String,
        method: RequestBody,
        kyat: RequestBody?,
        pae: RequestBody?,
        ywae: RequestBody?,
        goldSmithId: RequestBody?,
        bonus: RequestBody?,
        jewellerySizeId: RequestBody?,
        productIds: List<RequestBody>
    ): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        collectStockDataSource.collectBatch(
                            token,
                            method,
                            kyat,
                            pae,
                            ywae,
                            goldSmithId,
                            bonus,
                            jewellerySizeId,
                            productIds
                        ).response.asDomain()
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


//    override fun getProductIdList(
//        token: String,
//        productCodeList: List<RequestBody>
//    ): Flow<Resource<List<String>>>  =
//        flow {
//            emit(Resource.Loading())
//            try {
//                emit(
//                    Resource.Success(
//                        collectStockDataSource.getProductIdList(token, productCodeList)
//                    )
//                )
//            }catch (e: HttpException) {
//                emit(Resource.Error(GetErrorMessage.fromException(e)))
//            } catch (e: IOException) {
//                emit(Resource.Error(GetErrorMessage.fromException(e)))
//            } catch (e: Exception) {
//                emit(Resource.Error(e.message ?: "Unhandled Error"))
//            }
//        }

}