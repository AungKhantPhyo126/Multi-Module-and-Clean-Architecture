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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class CollectStockViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getProductIdUseCase: GetProductIdUseCase,
    private val collectSingleUseCase: CollectSingleUseCase,
    private val scanProductCodeUseCase: ScanProductCodeUseCase
//    private val getProductIdListUseCase: GetProductIdListUseCase
) : ViewModel() {
    var stockIdForSingle = ""
    private var _scannedStockcodebatch = MutableLiveData<MutableList<CollectStockBatchUIModel>>()
    val scannedStockcodebatch: LiveData<MutableList<CollectStockBatchUIModel>>
        get() = _scannedStockcodebatch
    fun resetScannedStockCodeBatch(){
        _scannedStockcodebatch.value= null
    }

    private var _getProductIdLiveData = MutableLiveData<Resource<String>>()
    val getProductIdLiveData: LiveData<Resource<String>>
        get() = _getProductIdLiveData

    fun resetGetProductIdLiveData(){
        _getProductIdLiveData.value = null
    }

    private var _scanProductCodeLive = MutableLiveData<Resource<ProductIdWithTypeDomain>>()
    val scanProductCodeLive: LiveData<Resource<ProductIdWithTypeDomain>>
        get() = _scanProductCodeLive

    fun resetScanProductCodeLive(){
        _scanProductCodeLive.value= null
    }

    private var _collectStockSingleLiveData = MutableLiveData<Resource<String>>()
    val collectStockSingleLiveData: LiveData<Resource<String>>
        get() = _collectStockSingleLiveData

    fun resetCollectStockSingleLiveData(){
        _collectStockSingleLiveData.value = null
    }


    var stockCodeList = mutableListOf<CollectStockBatchUIModel>()

    fun addStockCode(stockItem: CollectStockBatchUIModel) {
        stockCodeList.add(stockItem)
        _scannedStockcodebatch.value = stockCodeList
    }

    fun removeStockCode(item: CollectStockBatchUIModel) {
        stockCodeList.remove(item)
        _scannedStockcodebatch.value = stockCodeList
    }

    fun scanStock(code: String) {
        viewModelScope.launch {
            scanProductCodeUseCase(localDatabase.getToken().orEmpty(), code).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _scanProductCodeLive.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _scanProductCodeLive.value = Resource.Success(it.data)

                    }
                    is Resource.Error -> {
                        _scanProductCodeLive.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun getProductId(productCode: String) {
        viewModelScope.launch {
            scanProductCodeUseCase(
                localDatabase.getToken().orEmpty(),
                productCode
            ).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _getProductIdLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _getProductIdLiveData.value = Resource.Success(it.data!!.id)

                    }
                    is Resource.Error -> {
                        _getProductIdLiveData.value = Resource.Error(it.message)
                    }
                }
            }
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

    fun collectStock(productCode: String, weight: String) {
        val weightRequestBody = weight.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        viewModelScope.launch {
                collectSingleUseCase(
                    localDatabase.getToken().orEmpty(),
                    productCode,
                    weightRequestBody
                ).collectLatest {
                    when (it) {
                        is Resource.Loading -> {
                            _collectStockSingleLiveData.value = Resource.Loading()
                        }
                        is Resource.Success -> {
                            _collectStockSingleLiveData.value = Resource.Success(it.data)

                        }
                        is Resource.Error -> {
                            _collectStockSingleLiveData.value = Resource.Error(it.message)
                        }
                    }
                }
        }
    }


}