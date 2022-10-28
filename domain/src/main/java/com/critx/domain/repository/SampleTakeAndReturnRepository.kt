package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.SimpleData
import com.critx.domain.model.sampleTakeAndReturn.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface SampleTakeAndReturnRepository {

     fun scanInvoice(token:String,invoiceCode:String):Flow<Resource<VoucherScanDomain>>

     fun getVoucherSample(token: String,invoiceId:String):Flow<Resource<List<VoucherSampleDomain>>>

     fun getOutsideSample(token:String):Flow<Resource<List<VoucherSampleDomain>>>

     fun getInventorySample(token:String):Flow<Resource<List<InventorySampleDomain>>>

     fun checkSample(token: String,invoiceId: String):Flow<Resource<List<SampleCheckDomain>>>

     fun saveSample(token: String ,sample:HashMap<String,String>):Flow<Resource<SimpleData>>

     fun getHandedList(token: String):Flow<Resource<List<HandedListDomain>>>

     fun addToHandedList(token:String,sampleId:List<String>):Flow<Resource<SimpleData>>

     fun removeFromHandedList(token:String,sampleId: List<String>):Flow<Resource<SimpleData>>

     fun saveOutsideSample(token: String,name:RequestBody?,weight:RequestBody?,specification:RequestBody?,image: MultipartBody.Part):Flow<Resource<SimpleData>>

     fun returnSample(token: String,sampleId:List<String>):Flow<Resource<SimpleData>>
}