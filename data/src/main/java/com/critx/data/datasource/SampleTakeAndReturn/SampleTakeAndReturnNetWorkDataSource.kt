package com.critx.data.datasource.SampleTakeAndReturn

import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.SimpleResponseWithData
import com.critx.data.network.dto.sampleTakeAndReturn.*
import com.critx.domain.model.sampleTakeAndReturn.OutsideSampleDomain
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response

interface SampleTakeAndReturnNetWorkDataSource {
    suspend fun scanInvoice(token:String,invoiceCode:String):VoucherScanDto

    suspend fun getVoucherSample(token: String,invoiceId:String):List<VoucherSampleDto>

    suspend fun getOutsideSample(token:String):List<OutsideSampleDto>

    suspend fun getInventorySample(token:String):List<OutsideSampleDto>

    suspend fun checkSample(token: String,invoiceId: String):SampleCheckDto

    suspend fun checkSampleWithVoucher(token: String,invoiceId: String):List<SampleCheckDto>

    suspend fun saveSample(token: String ,sample:HashMap<String,String>):SimpleResponseDto

    suspend fun getHandedList(token: String):List<HandedListDto>

    suspend fun addToHandedList(token:String,sampleId:List<String>):SimpleResponseDto

    suspend fun removeFromHandedList(token:String,sampleId: String):SimpleResponseDto

    suspend fun saveOutsideSample(token: String,name:RequestBody?,weight:RequestBody?,specification:RequestBody?,image:MultipartBody.Part):SampleCheckDto

    suspend fun returnSample(token: String,sampleId:List<String>):SimpleResponseDto
}