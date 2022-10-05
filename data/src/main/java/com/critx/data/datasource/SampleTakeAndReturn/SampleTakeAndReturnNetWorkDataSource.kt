package com.critx.data.datasource.SampleTakeAndReturn

import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.sampleTakeAndReturn.HandedListDto
import com.critx.data.network.dto.sampleTakeAndReturn.SampleCheckDto
import com.critx.data.network.dto.sampleTakeAndReturn.VoucherSampleDto
import com.critx.data.network.dto.sampleTakeAndReturn.VoucherScanDto
import okhttp3.MultipartBody
import okhttp3.Response

interface SampleTakeAndReturnNetWorkDataSource {
    suspend fun scanInvoice(token:String,invoiceCode:String):VoucherScanDto

    suspend fun getVoucherSample(token: String,invoiceId:String):List<VoucherSampleDto>

    suspend fun getOutsideSample(token:String):List<VoucherSampleDto>

    suspend fun checkSample(token: String,invoiceId: String):List<SampleCheckDto>

    suspend fun saveSample(token: String ,sample:HashMap<String,String>):SimpleResponseDto

    suspend fun getHandedList(token: String):List<HandedListDto>

    suspend fun addToHandedList(token:String,sampleId:List<String>):SimpleResponseDto

    suspend fun removeFromHandedList(token:String,sampleId: List<String>):SimpleResponseDto

    suspend fun saveOutsideSample(token: String,name:String,weight:String,specification:String,image:MultipartBody.Part):SimpleResponseDto

    suspend fun returnSample(token: String,sampleId:List<String>):SimpleResponseDto
}