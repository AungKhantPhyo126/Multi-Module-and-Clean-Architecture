package com.critx.shwemiAdmin.screens.discount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.domain.model.DiscountVoucherScanDomain
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
import com.critx.domain.useCase.voucher.ScanDiscountVoucherUseCase
import com.critx.shwemiAdmin.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscountViewModel @Inject constructor(
    private val scanDiscountVoucherUseCase: ScanDiscountVoucherUseCase,
    private val localDatabase: LocalDatabase
) :ViewModel() {
    private var _scanDiscountVoucherLiveData = SingleLiveEvent<Resource<DiscountVoucherScanDomain>>()
    val scanDiscountVoucherLiveData: SingleLiveEvent<Resource<DiscountVoucherScanDomain>>
        get() = _scanDiscountVoucherLiveData

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
}