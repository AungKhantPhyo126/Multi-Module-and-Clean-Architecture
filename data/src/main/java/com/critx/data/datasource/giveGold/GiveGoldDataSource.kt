package com.critx.data.datasource.giveGold

import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.giveGold.GoldBoxDto
import retrofit2.http.Field
import retrofit2.http.Header

interface GiveGoldDataSource {
    suspend fun giveGold(
        token: String,
        goldSmithId: String,
        orderItem: String,
        orderQty: String,
        weightK: String,
        weighP: String,
        weightY: String,
        goldBoxId: String,
        goldWeight: String,
        gemWeight: String,
        wastageK: String,
        wastageP: String,
        wastageY: String,
        dueDate: String,
        sampleList: List<String>
    ):SimpleResponseDto

    suspend fun getGoldBoxId(
        token: String
    ):List<GoldBoxDto>

    suspend fun serviceCharge(
        token: String,
        chargeAmount:String,
        wastageGm:String,
        invoice:String
    ):SimpleResponseDto
}