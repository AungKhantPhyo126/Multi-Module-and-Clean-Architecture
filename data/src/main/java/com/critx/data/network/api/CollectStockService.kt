package com.critx.data.network.api

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.collectStock.GoldSmithListResponse
import com.critx.data.network.dto.collectStock.JewellerySizeResponse
import com.critx.data.network.dto.collectStock.ProductIdListResponse
import com.critx.data.network.dto.collectStock.ProductIdResponse
import com.critx.data.network.dto.setupStock.ProductCodeResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface CollectStockService {

    @GET("api/products/update/weight_with_label/{productCode}")
    suspend fun getProductId(
        @Header("Authorization") token: String,
        @Path("productCode") productCode:String
    ): Response<ProductIdResponse>

    @Multipart
    @POST("api/products/update/weight_with_label/{stockCode}")
    suspend fun singleStockUpdate(
        @Header("Authorization") token: String,
        @Path("stockCode") stockCode:String,
        @Part("weight_including_label_gm") weight:RequestBody
    ): Response<SimpleResponse>

//    @GET("api/scan/product_codes")
//    suspend fun getProductIdList(
//        @Header("Authorization") token: String,
//        @Part("product_codes[]") productCodeList:List<RequestBody>
//    ):Response<ProductIdListResponse>

    @GET("api/products/{stockCode}/scan")
    suspend fun scanStockCode(
        @Header("Authorization") token: String,
        @Path("stockCode") stockCode:String,
        ):Response<ProductIdListResponse>

    @GET("api/sizes/quicklist")
    suspend fun getJewellerySize(
        @Header("Authorization") token: String,
        @Query("jewellery_type") type:String
        ):Response<JewellerySizeResponse>

    @JvmSuppressWildcards
    @Multipart
    @POST("api/products/update/multiple")
    suspend fun multipleStockUpdate(
        @Header("Authorization") token: String,
        @Part ("_method") method:RequestBody,
        @Part("wastage_kyat") avgWastageKyat: RequestBody?,
        @Part("wastage_pae") avgWastagePae: RequestBody?,
        @Part("wastage_ywae") avgWastagYwae: RequestBody?,
        @Part("goldsmith_id") goldSmithId: RequestBody?,
        @Part("bonus") bonus: RequestBody?,
        @Part("jewellery_type_size_id") jewelleryTypeSizeId: RequestBody?,
        @Part("products[]") productIds: List<RequestBody>,
        ):Response<SimpleResponse>

    @GET("api/goldsmiths/quicklist")
    suspend fun getGoldSmitList(
        @Header("Authorization") token: String,
        @Query("type") type:String,
    ):Response<GoldSmithListResponse>
}