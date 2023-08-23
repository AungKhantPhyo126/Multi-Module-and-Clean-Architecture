package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.SimpleData
import com.critx.domain.model.giveGold.GiveGoldScanDomain
import com.critx.domain.model.giveGold.GoldBoxDomain
import kotlinx.coroutines.flow.Flow

interface GiveGoldRepository {
     fun giveGold(
        token: String,
        goldSmithId: String,
        orderItem: String,
        orderQty: String,
        weightY: String,
        goldBoxId: String,
        goldWeight: String,
        gemWeight: String,
        goldAndGemWeight:String,
        wastageY: String,
        dueDate: String?,
        sampleList: List<String>?
    ):Flow<Resource<String>>

     fun getGoldBoxId(
        token: String
    ):Flow<Resource<List<GoldBoxDomain>>>

     fun serviceCharge(
        token: String,
        chargeAmount:String,
        wastageGm:String,
        invoice:String
    ):Flow<Resource<SimpleData>>

     fun giveGoldScan(
         token: String,
         invoiceNumber:String,
     ):Flow<Resource<GiveGoldScanDomain>>

    fun getPdfPrint(
        token: String,
        voucherID: String
    ): Flow<Resource<String>>
}