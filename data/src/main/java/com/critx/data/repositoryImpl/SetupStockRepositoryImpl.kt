package com.critx.data.repositoryImpl

import com.critx.commonkotlin.util.Resource
import com.critx.data.GetErrorMessage
import com.critx.data.datasource.setupstock.SetupStockNetWorkDatasource
import com.critx.data.network.dto.asDomain
import com.critx.data.network.dto.setupStock.asDomain
import com.critx.data.network.dto.setupStock.jewelleryCategory.asDomain
import com.critx.data.network.dto.setupStock.jewelleryGroup.asDomain
import com.critx.data.network.dto.setupStock.jewelleryQuality.asDomain
import com.critx.data.network.dto.setupStock.jewelleryType.asDomain
import com.critx.domain.model.SetupStock.JewelleryType.JewelleryType
import com.critx.domain.model.SetupStock.ProductCodeDomain
import com.critx.domain.model.SetupStock.jewelleryCategory.CalculateKPY
import com.critx.domain.model.SetupStock.jewelleryCategory.DesignDomain
import com.critx.domain.model.SetupStock.jewelleryCategory.JewelleryCategory
import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroup
import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroupDomain
import com.critx.domain.model.SetupStock.jewelleryQuality.JewelleryQuality
import com.critx.domain.model.SimpleData
import com.critx.domain.repository.SetupStockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SetupStockRepositoryImpl @Inject constructor(
    private val setupStockNetWorkDatasource: SetupStockNetWorkDatasource
) : SetupStockRepository {
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
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unhandled Error"))
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
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unhandled Error"))
            }
        }

    override fun getJewelleryGroup(token: String,frequentUse:Int,firstCatId:Int,secondCatId:Int): Flow<Resource<JewelleryGroupDomain>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        setupStockNetWorkDatasource.getJewelleryGroup(token,frequentUse,firstCatId,secondCatId).asDomain()
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

    override fun createJewelleryGroup(
        token: String,
        image: MultipartBody.Part,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        is_frequently_used: RequestBody,
        name: RequestBody
    ): Flow<Resource<JewelleryGroup>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        setupStockNetWorkDatasource.createJewelleryGroup(
                            token,
                            image, jewellery_type_id, jewellery_quality_id, is_frequently_used, name
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

    override fun editJewelleryGroup(
        token: String,
        method:RequestBody,
        groupId: String,
        image: MultipartBody.Part?,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        is_frequently_used: RequestBody,
        name: RequestBody
    ): Flow<Resource<SimpleData>>  =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        setupStockNetWorkDatasource.editJewelleryGroup(
                            token,method,
                            groupId,
                            image, jewellery_type_id, jewellery_quality_id, is_frequently_used, name
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

    override fun deleteJewelleryGroup(
        token: String,
        method: RequestBody,
        groupId: String
    ): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        setupStockNetWorkDatasource.deleteJewelleryGroup(
                            token,method,
                            groupId,
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

    override fun deleteJewelleryCategory(
        token: String,
        method: RequestBody,
        catId: String
    ): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        setupStockNetWorkDatasource.deleteJewelleryCategory(
                            token,method,
                            catId,
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

    override fun getJewelleryCategory(
        token: String,
        frequentUse: Int?,
        firstCatId: Int?,
        secondCatId: Int?,
        thirdCatId: Int?
    ): Flow<Resource<List<JewelleryCategory>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        setupStockNetWorkDatasource.getJewelleryCategory(token,frequentUse,firstCatId,secondCatId,thirdCatId).data.map { it.asDomain() }
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

    override fun createJewelleryCategory(
        token: String,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        groupId: RequestBody,
        is_frequently_used: RequestBody,
        withGem:RequestBody,
        name: RequestBody,
        avgWeigh: RequestBody,
        avgKyat:RequestBody,
        avgPae:RequestBody,
        avgYwae:RequestBody,
        images: MutableList<MultipartBody.Part>,
        video: MultipartBody.Part?,
        specification: RequestBody,
        design: MutableList<RequestBody>,
        orderToGs:RequestBody,
        recommendCat:MutableList<RequestBody>?

    ): Flow<Resource<JewelleryCategory>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        setupStockNetWorkDatasource.createJewelleryCategory(
                            token, jewellery_type_id, jewellery_quality_id, groupId, is_frequently_used,withGem, name, avgWeigh, avgKyat,avgPae,avgYwae, images, video, specification, design,orderToGs
                        ,recommendCat).data.asDomain()
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

    override fun editJewelleryCategory(
        token: String,
        method: RequestBody,
        categoryId: String,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        groupId: RequestBody,
        is_frequently_used: RequestBody,
        withGem: RequestBody,
        name: RequestBody,
        avgWeigh: RequestBody,
        avgKyat:RequestBody,
        avgPae:RequestBody,
        avgYwae:RequestBody,
        images: MutableList<MultipartBody.Part>,
        video: MultipartBody.Part?,
        specification: RequestBody,
        design: MutableList<RequestBody>,
        orderToGs: RequestBody,
        recommendCat: MutableList<RequestBody>
    ): Flow<Resource<SimpleData>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        setupStockNetWorkDatasource.editJewelleryCategory(
                            token, method,categoryId,jewellery_type_id, jewellery_quality_id, groupId, is_frequently_used,withGem, name, avgWeigh, avgKyat,avgPae,avgYwae, images, video, specification, design,orderToGs
                            ,recommendCat).response.asDomain()
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

    override fun getRelatedCategories(
        token: String,
        categoryId: String
    ): Flow<Resource<List<JewelleryCategory>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        setupStockNetWorkDatasource.getRelatedJewelleryCategories(token,categoryId).data.map { it.asDomain() }
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

    override fun calculateKPYtoGram(
        token: String,
        kyat: Double,
        pae: Double,
        ywae: Double
    ): Flow<Resource<CalculateKPY>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        setupStockNetWorkDatasource.calculateKPYtoGram(
                            token, kyat, pae, ywae
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

    override fun getDesignList(token: String,jewelleryType:String): Flow<Resource<List<DesignDomain>>> =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        setupStockNetWorkDatasource.getDesign(
                            token,
                            jewelleryType
                        ).data.map { it.asDomain() }
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

    override fun createProduct(
        token: String,
        name: RequestBody?,
        productCode:RequestBody,
        type: RequestBody,
        quality: RequestBody,
        group: RequestBody?,
        categoryId: RequestBody?,
        goldAndGemWeight: RequestBody?,
        gemWeightKyat: RequestBody?,
        gemWeightPae: RequestBody?,
        gemWeightYwae: RequestBody?,
        gemValue: RequestBody?,
        ptAndClipCost: RequestBody?,
        maintenanceCost: RequestBody?,
        diamondInfo: RequestBody?,
        diamondPriceFromGS: RequestBody?,
        diamondValueFromGS: RequestBody?,
        diamondPriceForSale: RequestBody?,
        diamondValueForSale: RequestBody?,
        images: List<MultipartBody.Part>,
        video: MultipartBody.Part?
    ): Flow<Resource<SimpleData>>  =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        setupStockNetWorkDatasource.createProduct(
                            token,name,productCode, type, quality, group, categoryId, goldAndGemWeight, gemWeightKyat, gemWeightPae, gemWeightYwae, gemValue, ptAndClipCost, maintenanceCost, diamondInfo, diamondPriceFromGS, diamondValueFromGS, diamondPriceForSale, diamondValueForSale, images, video
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

    override fun getProductCode(token: String): Flow<Resource<ProductCodeDomain>>  =
        flow {
            emit(Resource.Loading())
            try {
                emit(
                    Resource.Success(
                        setupStockNetWorkDatasource.getProductCode(
                            token
                        ).data.asDomain()
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