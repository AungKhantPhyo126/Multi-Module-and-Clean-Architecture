package com.critx.data.datasource.giveGold

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.SimpleResponseWithStringData
import com.critx.data.network.dto.giveGold.GiveGoldScanDto
import com.critx.data.network.dto.giveGold.GoldBoxDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GiveGoldDataSource {
    suspend fun giveGold(
        token: String,
        goldSmithId: String,
        orderItem: String,
        orderQty: String,
        weightY: String,
        goldBoxId: String,
        goldWeight: String,
        goldAndGemWeight: String,
        gemWeight: String,
        wastageY: String,
        dueDate: String?,
        sampleList: List<String>?
    ): String

    suspend fun getGoldBoxId(
        token: String
    ): List<GoldBoxDto>

    suspend fun serviceCharge(
        token: String,
        chargeAmount: String,
        wastageGm: String,
        invoice: String
    ): SimpleResponseDto

    suspend fun giveGoldScan(
        token: String,
        invoiceNumber: String
    ): GiveGoldScanDto

    suspend fun getPdfPrint(
        token: String,
        voucherID: String
    ): String
}