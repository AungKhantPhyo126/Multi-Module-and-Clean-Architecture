package com.critx.shwemiAdmin.screens.discount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.domain.model.DiscountVoucherScanDomain
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
import com.critx.domain.useCase.AddDiscountUseCase
import com.critx.domain.useCase.voucher.ScanDiscountVoucherUseCase
import com.critx.shwemiAdmin.SingleLiveEvent
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel
import com.critx.shwemiAdmin.uiModel.discount.DiscountUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscountViewModel @Inject constructor(
    private val scanDiscountVoucherUseCase: ScanDiscountVoucherUseCase,
    private val localDatabase: LocalDatabase,
    private val addDiscountUseCase: AddDiscountUseCase
) :ViewModel() {
    private var _scanDiscountVoucherLiveData = SingleLiveEvent<Resource<DiscountVoucherScanDomain>>()
    val scanDiscountVoucherLiveData: SingleLiveEvent<Resource<DiscountVoucherScanDomain>>
        get() = _scanDiscountVoucherLiveData

    private var _addDiscountLiveData = SingleLiveEvent<Resource<String>>()
    val addDiscountLiveData: SingleLiveEvent<Resource<String>>
        get() = _addDiscountLiveData

    private var _voucherCodesLiveData = MutableLiveData<MutableList<DiscountVoucherScanDomain>>()
    val voucherCodesLiveData: LiveData<MutableList<DiscountVoucherScanDomain>>
        get() = _voucherCodesLiveData

    fun scanStock(code: String) {
        viewModelScope.launch {
            scanDiscountVoucherUseCase(localDatabase.getToken().orEmpty(), code).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _scanDiscountVoucherLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _scanDiscountVoucherLiveData.value = Resource.Success(it.data)

                    }
                    is Resource.Error -> {
                        _scanDiscountVoucherLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun addDiscount(
        voucherCodes:List<String>,
        amount:String
    ){
        viewModelScope.launch {
            addDiscountUseCase(localDatabase.getToken().orEmpty(),
            voucherCodes,amount).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _addDiscountLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _addDiscountLiveData.value = Resource.Success(it.data?.message.orEmpty())

                    }
                    is Resource.Error -> {
                        _addDiscountLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    var voucherCodesList = mutableListOf<DiscountVoucherScanDomain>()

    fun addStockCode(stockItem: DiscountVoucherScanDomain) {
        voucherCodesList.add(stockItem)
        _voucherCodesLiveData.value = voucherCodesList
    }

    fun removeStockCode(item: DiscountVoucherScanDomain) {
        voucherCodesList.remove(item)
        _voucherCodesLiveData.value = voucherCodesList
    }

    fun resetStockCodes(){
        voucherCodesList = mutableListOf()
        _voucherCodesLiveData.value = voucherCodesList
    }
}