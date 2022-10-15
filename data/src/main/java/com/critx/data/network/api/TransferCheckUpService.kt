package com.critx.data.network.api

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.transferCheckUp.CheckUpResponse
import retrofit2.Response
import retrofit2.http.*

interface TransferCheckUpService {
    @FormUrlEncoded
    @POST("api/boxes/{boxCode}/check-up")
    suspend fun checkUp(
        @Header("Authorization") token: String,
        @Path("boxCode")boxCode:String,
        @Field("product_id[]") productIdList:List<String>,
    ): Response<CheckUpResponse>

    @FormUrlEncoded
    @POST("api/boxes/{boxCode}/transfer")
    suspend fun transfer(
        @Header("Authorization") token: String,
        @Path("boxCode")boxCode:String,
        @Field("product_id[]") productIdList:List<String>,
        @FieldMap rfidCode:HashMap<String,String>,
    ): Response<SimpleResponse>
}