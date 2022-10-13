package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.new

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
import com.critx.domain.model.sampleTakeAndReturn.VoucherSampleDomain
import com.critx.domain.model.sampleTakeAndReturn.VoucherScanDomain
import com.critx.domain.useCase.collectStock.ScanProductCodeUseCase
import com.critx.domain.useCase.sampleTakeAndReturn.CheckSampleUseCase
import com.critx.domain.useCase.sampleTakeAndReturn.ScanInvoiceUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.notifyObserverWithResource
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.VoucherForSampleUIModel
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.asUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val scanInvoiceUseCase: ScanInvoiceUseCase,
    private val checkSampleUseCase: CheckSampleUseCase,
    private val scanProductCodeUseCase: ScanProductCodeUseCase
) :ViewModel(){

    private val _sampleLiveData = MutableLiveData<Resource<MutableList<SampleItemUIModel>>>()
    val sampleLiveData:LiveData<Resource<MutableList<SampleItemUIModel>>>
        get() = _sampleLiveData
    private var _scanProductCodeLive = MutableLiveData<Resource<ProductIdWithTypeDomain>>()
    val scanProductCodeLive: LiveData<Resource<ProductIdWithTypeDomain>>
        get() = _scanProductCodeLive

    fun resetScanProductCodeLive(){
        _scanProductCodeLive.value= null
    }

    fun addStockSample(item: SampleItemUIModel){
        _sampleLiveData.value?.data?.add(item)
        _sampleLiveData.notifyObserverWithResource()
    }

    fun removeSample(item: SampleItemUIModel){
        _sampleLiveData.value?.data?.remove(item)
        _sampleLiveData.notifyObserverWithResource()

    }

    private val _voucherScanLiveData = MutableLiveData<Resource<VoucherScanDomain>>()
    val voucherScanLiveData:LiveData<Resource<VoucherScanDomain>>
        get() = _voucherScanLiveData

    fun scanVoucher(invoiceCode:String){
        viewModelScope.launch {
            scanInvoiceUseCase(localDatabase.getToken().orEmpty(),invoiceCode).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _voucherScanLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _voucherScanLiveData.value = Resource.Success(it.data)
                        checkSampleUseCase(localDatabase.getToken().orEmpty(),it.data!!.id)
                    }
                    is Resource.Error->{
                        _voucherScanLiveData.value = Resource.Error(it.message)
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

    fun checkSampleUseCase(stockId:String){
        viewModelScope.launch {
            checkSampleUseCase(localDatabase.getToken().orEmpty(), stockId).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _sampleLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _sampleLiveData.value = Resource.Success(it.data.map { it.asUIModel() })
                    }
                    is Resource.Error -> {
                        _sampleLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }
}