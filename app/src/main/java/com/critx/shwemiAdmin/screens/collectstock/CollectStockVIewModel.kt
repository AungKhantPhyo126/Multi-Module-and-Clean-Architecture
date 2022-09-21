package com.critx.shwemiAdmin.screens.collectstock

import androidx.lifecycle.*
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
import com.critx.domain.useCase.collectStock.CollectSingleUseCase
import com.critx.domain.useCase.collectStock.GetProductIdUseCase
import com.critx.domain.useCase.collectStock.ScanProductCodeUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class CollectStockVIewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getProductIdUseCase: GetProductIdUseCase,
    private val collectSingleUseCase: CollectSingleUseCase,
    private val scanProductCodeUseCase: ScanProductCodeUseCase
//    private val getProductIdListUseCase: GetProductIdListUseCase
) :ViewModel(){
    private var _scannedStockcodebatch=MutableLiveData<MutableList<CollectStockBatchUIModel>>()
    val scannedStockcodebatch : LiveData<MutableList<CollectStockBatchUIModel>>
    get() = _scannedStockcodebatch

    private var _getProductIdLiveData = MutableLiveData<Resource<String>>()
    val getProductIdLiveData:LiveData<Resource<String>>
    get() = _getProductIdLiveData

    private var _scanProductCodeLive = MutableLiveData<Resource<ProductIdWithTypeDomain>>()
    val scanProductCodeLive:LiveData<Resource<ProductIdWithTypeDomain>>
        get() = _scanProductCodeLive

    private var _collectStockSingleLiveData = MutableLiveData<Resource<String>>()
    val collectStockSingleLiveData:LiveData<Resource<String>>
        get() = _collectStockSingleLiveData


    private val stockCodeList = mutableListOf<CollectStockBatchUIModel>()

    fun addStockCode(stockItem:CollectStockBatchUIModel){
        stockCodeList.add(stockItem)
        _scannedStockcodebatch.value=stockCodeList
    }

    fun removeStockCode(item:CollectStockBatchUIModel){
        stockCodeList.remove(item)
        _scannedStockcodebatch.value=stockCodeList
    }

    fun scanStock(code: String){
        viewModelScope.launch {
            _scanProductCodeLive = scanProductCodeUseCase(localDatabase.getToken().orEmpty(),code).asLiveData() as MutableLiveData<Resource<ProductIdWithTypeDomain>>
        }
    }

    fun getProductId(productCode:String){
        viewModelScope.launch {
            _getProductIdLiveData =
                getProductIdUseCase(localDatabase.getToken().orEmpty(),productCode).asLiveData() as MutableLiveData<Resource<String>>
        }
    }


//    fun getProductIdList(){
//        var list = mutableListOf<RequestBody>()
//        _scannedStockcodebatch.value?.map {
//            it.invoiceCode
//        }?.forEach {
//            list.add(it.toRequestBody("multipart/form-data".toMediaTypeOrNull()))
//        }
//        viewModelScope.launch {
//            _getProductIdListLiveData =
//                getProductIdListUseCase(localDatabase.getToken().orEmpty(),list).asLiveData() as MutableLiveData<Resource<List<String>>>
//        }
//    }

    fun collectStock(productCode:String,weight:String){
        val weightRequestBody = weight.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        viewModelScope.launch {
            _collectStockSingleLiveData =
                collectSingleUseCase(localDatabase.getToken().orEmpty(),productCode,weightRequestBody).asLiveData() as MutableLiveData<Resource<String>>
        }
    }



}