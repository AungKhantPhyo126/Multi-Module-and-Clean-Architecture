package com.critx.data.network.api

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.giveGold.GoldBoxResponse
import retrofit2.Response
import retrofit2.http.*

interface GiveGoldService {

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("api/gold_givings/store")
    suspend fun giveGold(
        @Header("Authorization") token: String,
        @Field("goldsmith_id") goldSmithId: String,
        @Field("order_item") orderItem: String,
        @Field("order_qty") orderQty: String,
        @Field("weight_kyat_per_unit") weightK: String,
        @Field("weight_pae_per_unit") weighP: String,
        @Field("weight_ywae_per_unit") weightY: String,
        @Field("gold_box_id") goldBoxId: String,
        @Field("gold_weight_gm") goldWeight: String,
        @Field("gem_weight_gm") gemWeight: String,
        @Field("gold_gem_weight_gm") goldAndGemWeight: String,
        @Field("wastage_kyat") wastageK: String,
        @Field("wastage_pae") wastageP: String,
        @Field("wastage_ywae") wastageY: String,
        @Field("due_date") dueDate: String,
        @Field("samples[]") sampleList: List<String>?
    ): Response<SimpleResponse>

    @GET("api/gold_boxes/quicklist")
    suspend fun getGoldBoxId(
        @Header("Authorization") token: String,
    ): Response<GoldBoxResponse>

    @FormUrlEncoded
    @GET("api/gold_givings/{invoice}/retrieve-products")
    suspend fun serviceCharge(
        @Header("Authorization") token: String,
        @Field("charged_amount") chargeAmount: String,
        @Field("wastage_gm") wastageGm: String,
        @Path("invoice") invoice: String
    ): Response<SimpleResponse>
}