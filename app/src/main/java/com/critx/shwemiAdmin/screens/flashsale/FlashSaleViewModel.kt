package com.critx.shwemiAdmin.screens.flashsale

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.data.network.dto.collectStock.ProductId
import com.critx.data.network.dto.setupStock.ProductCodeData
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
import com.critx.domain.useCase.collectStock.ScanProductCodeUseCase
import com.critx.domain.useCase.flashSales.AddFlashSaleUseCase
import com.critx.shwemiAdmin.SingleLiveEvent
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryTypeUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FlashSaleViewModel @Inject constructor(
    private val addFlashSaleUseCase: AddFlashSaleUseCase,
    private val scanProductCodeUseCase: ScanProductCodeUseCase,
    private val localDatabase: LocalDatabase
) :ViewModel() {
    var selectedImgUri: File? = null

    private var _addFlashSaleLiveData= SingleLiveEvent<Resource<String>>()
    val addFlashSaleLiveData : SingleLiveEvent<Resource<String>>
        get() = _addFlashSaleLiveData

    private var _scanProductCodeLive = SingleLiveEvent<Resource<ProductIdWithTypeDomain>>()
    val scanProductCodeLive: SingleLiveEvent<Resource<ProductIdWithTypeDomain>>
        get() = _scanProductCodeLive

    private var _scannedStockcodes = MutableLiveData<MutableList<CollectStockBatchUIModel>>()
    val scannedStockcodes: LiveData<MutableList<CollectStockBatchUIModel>>
        get() = _scannedStockcodes

    fun addFlashSale(
        title: RequestBody,
        discount_amount: RequestBody,
        time_from: RequestBody,
        time_to: RequestBody,
        productIds: List<RequestBody>,
        image: MultipartBody.Part,
    ){
        viewModelScope.launch {
            addFlashSaleUseCase(
                localDatabase.getToken().orEmpty(),
                title, discount_amount, time_from, time_to, productIds, image
            ).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _addFlashSaleLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _addFlashSaleLiveData.value = Resource.Success(it.data!!.message)
                    }
                    is Resource.Error->{
                        _addFlashSaleLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
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
    var stockCodeList = mutableListOf<CollectStockBatchUIModel>()

    fun addStockCode(stockItem: CollectStockBatchUIModel) {
        stockCodeList.add(stockItem)
        _scannedStockcodes.value = stockCodeList
    }

    fun removeStockCode(item: CollectStockBatchUIModel) {
        stockCodeList.remove(item)
        _scannedStockcodes.value = stockCodeList
    }

    fun resetStockCodes(){
        stockCodeList = mutableListOf()
        _scannedStockcodes.value = stockCodeList
    }

}