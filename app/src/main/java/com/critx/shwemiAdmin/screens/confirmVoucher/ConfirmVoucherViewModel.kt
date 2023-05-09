package com.critx.shwemiAdmin.screens.confirmVoucher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.domain.model.DiscountVoucherScanDomain
import com.critx.domain.model.StockInVoucherDomain
import com.critx.domain.model.voucher.ScanVoucherToConfirmDomain
import com.critx.domain.useCase.voucher.ConfirmVoucherUseCase
import com.critx.domain.useCase.voucher.GetStockInVoucherUseCase
import com.critx.domain.useCase.voucher.ScanVoucherToConfirmUseCase
import com.critx.shwemiAdmin.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmVoucherViewModel @Inject constructor(
    private val scanVoucherToConfirmUseCase: ScanVoucherToConfirmUseCase,
    private val confirmVoucherUseCase: ConfirmVoucherUseCase,
    private val getStockInVoucherUseCase: GetStockInVoucherUseCase,
    private val localDatabase: LocalDatabase
): ViewModel() {
    private var _getStocksInVoucherLiveData = SingleLiveEvent<Resource<List<StockInVoucherDomain>>>()
    val getStocksInVoucherLiveData: SingleLiveEvent<Resource<List<StockInVoucherDomain>>>
        get() = _getStocksInVoucherLiveData
    private var _scanVoucherLiveData = SingleLiveEvent<Resource<ScanVoucherToConfirmDomain>>()
    val scanVoucherLiveData: SingleLiveEvent<Resource<ScanVoucherToConfirmDomain>>
        get() = _scanVoucherLiveData

    private var _confirmVoucherLiveData = SingleLiveEvent<Resource<String>>()
    val confirmVoucherLiveData: SingleLiveEvent<Resource<String>>
        get() = _confirmVoucherLiveData

    fun scanStock(code: String) {
        viewModelScope.launch {
            scanVoucherToConfirmUseCase(localDatabase.getToken().orEmpty(), code).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _scanVoucherLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _scanVoucherLiveData.value = Resource.Success(it.data)

                    }
                    is Resource.Error -> {
                        _scanVoucherLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun confirmVoucher(code: String) {
        viewModelScope.launch {
            confirmVoucherUseCase(localDatabase.getToken().orEmpty(), code).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _confirmVoucherLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _confirmVoucherLiveData.value = Resource.Success(it.data)

                    }
                    is Resource.Error -> {
                        _confirmVoucherLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun getStocksInVoucher(code: String) {
        viewModelScope.launch {
            getStockInVoucherUseCase(localDatabase.getToken().orEmpty(), code).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _getStocksInVoucherLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _getStocksInVoucherLiveData.value = Resource.Success(it.data)

                    }
                    is Resource.Error -> {
                        _getStocksInVoucherLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }
}