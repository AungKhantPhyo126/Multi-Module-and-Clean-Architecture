package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.new

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.sampleTakeAndReturn.VoucherSampleDomain
import com.critx.domain.model.sampleTakeAndReturn.VoucherScanDomain
import com.critx.domain.useCase.sampleTakeAndReturn.CheckSampleUseCase
import com.critx.domain.useCase.sampleTakeAndReturn.ScanInvoiceUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val scanInvoiceUseCase: ScanInvoiceUseCase,
    private val checkSampleUseCase: CheckSampleUseCase
) :ViewModel(){

    private val _sampleLiveData = MutableLiveData<Resource<SampleItemUIModel>>()
    val sampleLiveData:LiveData<Resource<SampleItemUIModel>>
        get() = _sampleLiveData

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
}