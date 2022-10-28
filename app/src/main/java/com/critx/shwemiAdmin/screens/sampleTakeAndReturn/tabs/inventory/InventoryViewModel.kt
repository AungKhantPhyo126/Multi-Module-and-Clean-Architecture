package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
import com.critx.domain.model.sampleTakeAndReturn.HandedListDomain
import com.critx.domain.model.sampleTakeAndReturn.VoucherScanDomain
import com.critx.domain.useCase.collectStock.ScanProductCodeUseCase
import com.critx.domain.useCase.sampleTakeAndReturn.*
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.notifyObserverWithResource
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel
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
    private val scanProductCodeUseCase: ScanProductCodeUseCase,
    private val addToHandedListUseCase: AddToHandedListUseCase,
    private val saveSampleUseCase: SaveSampleUseCase,
    private val getHandedListUseCase: GetHandedListUseCase
) : ViewModel() {



    private val _sampleLiveData = MutableLiveData<Resource<MutableList<SampleItemUIModel>>>()
    val sampleLiveData: LiveData<Resource<MutableList<SampleItemUIModel>>>
        get() = _sampleLiveData

    private val _scannedSampleLiveData = MutableLiveData<MutableList<SampleItemUIModel>>()
    val scannedSampleLiveData: LiveData<MutableList<SampleItemUIModel>>
        get() = _scannedSampleLiveData

    private var _scanProductCodeLive = MutableLiveData<Resource<ProductIdWithTypeDomain>>()
    val scanProductCodeLive: LiveData<Resource<ProductIdWithTypeDomain>>
        get() = _scanProductCodeLive

    private val _addToHandedListLiveData = MutableLiveData<Resource<String>>()
    val addToHandedListLiveData: LiveData<Resource<String>>
        get() = _addToHandedListLiveData

    private val _saveSampleLiveData = MutableLiveData<Resource<String>>()
    val saveSampleLiveData: LiveData<Resource<String>>
        get() = _saveSampleLiveData

    fun resetScanProductCodeLive() {
        _scanProductCodeLive.value = null
    }

    var scannedSamples = mutableListOf<SampleItemUIModel>()
    var specificationList = mutableListOf<String>()

    fun addStockSample(item: SampleItemUIModel) {
        scannedSamples.add(item)
        specificationList.add("")
        _scannedSampleLiveData.value = scannedSamples
//        _scannedSampleLiveData.notifyObserverWithResource()
    }

    fun removeSample(item: SampleItemUIModel) {
        specificationList.removeAt(scannedSamples.indexOf(item))
        scannedSamples.remove(item)
        _scannedSampleLiveData.value = scannedSamples

//        _scannedSampleLiveData.notifyObserverWithResource()
    }

    fun resetSample() {
        scannedSamples.removeAll(scannedSamples)
        _scannedSampleLiveData.value = scannedSamples


    }

    private val _voucherScanLiveData = MutableLiveData<Resource<VoucherScanDomain>>()
    val voucherScanLiveData: LiveData<Resource<VoucherScanDomain>>
        get() = _voucherScanLiveData



    fun addToHandedList() {
        viewModelScope.launch {
            addToHandedListUseCase(
                localDatabase.getToken().orEmpty(),
                scannedSamples.map { it.id }).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _addToHandedListLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _addToHandedListLiveData.value = Resource.Success(it.data!!.message)
                    }
                    is Resource.Error -> {
                        _addToHandedListLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun saveSamples() {
        viewModelScope.launch {
            val sampleIdHashMap: HashMap<String, String> = HashMap()
            repeat(scannedSamples.filter { it.specification.isNullOrEmpty() }.map { it.id }.size) {
                sampleIdHashMap["sample[${scannedSamples.map { it.productId }[it]}]"] =
                    specificationList[it]
            }

            saveSampleUseCase(localDatabase.getToken().orEmpty(), sampleIdHashMap).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _saveSampleLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _saveSampleLiveData.value = Resource.Success(it.data!!.message)
                        repeat(scannedSamples.filter { it.specification.isNullOrEmpty() }
                            .size) {
                              scannedSamples[it].specification = specificationList[it]
                        }
                        _scannedSampleLiveData.value = scannedSamples
                    }
                    is Resource.Error -> {
                        _saveSampleLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun scanVoucher(invoiceCode: String) {
        viewModelScope.launch {
            scanInvoiceUseCase(localDatabase.getToken().orEmpty(), invoiceCode).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _voucherScanLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _voucherScanLiveData.value = Resource.Success(it.data)
                        checkSampleUseCase(localDatabase.getToken().orEmpty(), it.data!!.id)
                    }
                    is Resource.Error -> {
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

    fun checkSampleUseCase(stockId: String) {
        viewModelScope.launch {
            checkSampleUseCase(localDatabase.getToken().orEmpty(), stockId).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _sampleLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _sampleLiveData.value =
                            Resource.Success(it.data!!.map { it.asUIModel() } as MutableList<SampleItemUIModel>)
                    }
                    is Resource.Error -> {
                        _sampleLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }
}