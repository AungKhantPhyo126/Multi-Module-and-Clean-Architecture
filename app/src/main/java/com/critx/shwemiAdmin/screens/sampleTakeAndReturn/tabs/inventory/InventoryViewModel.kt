package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
import com.critx.domain.model.sampleTakeAndReturn.*
import com.critx.domain.useCase.collectStock.ScanProductCodeUseCase
import com.critx.domain.useCase.sampleTakeAndReturn.*
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
    private val checkSampleWithVoucherUseCase: CheckSampleWithVoucherUseCase,
    private val scanProductCodeUseCase: ScanProductCodeUseCase,
    private val addToHandedListUseCase: AddToHandedListUseCase,
    private val saveSampleUseCase: SaveSampleUseCase,
    private val getInventorySampleUseCase: GetInventorySampleUseCase,
    private val returnSampleUseCase: ReturnSampleUseCase
) : ViewModel() {

    var scannedProductCode = ""

    private val _getInventorySampleLiveData = MutableLiveData<Resource<List<OutsideSampleDomain>>>()
    val getInventorySampleLiveData: LiveData<Resource<List<OutsideSampleDomain>>>
        get() = _getInventorySampleLiveData

    private val _sampleLiveData = MutableLiveData<Resource<SampleItemUIModel>>()
    val sampleLiveData: LiveData<Resource<SampleItemUIModel>>
        get() = _sampleLiveData

    private val _sampleLiveDataFromVoucher = MutableLiveData<Resource<List<SampleItemUIModel>>>()
    val sampleLiveDataFromVoucher: LiveData<Resource<List<SampleItemUIModel>>>
        get() = _sampleLiveDataFromVoucher

    fun resetSampleLiveDataFromVoucher(){
        _sampleLiveDataFromVoucher.value = null
    }

    fun resetSampleLiveData(){
        _sampleLiveData.value = null
    }

    private val _scannedSampleForReturnLiveData = MutableLiveData<MutableList<SampleItemUIModel>>()
    val scannedSampleForReturnLiveData: LiveData<MutableList<SampleItemUIModel>>
        get() = _scannedSampleForReturnLiveData

    private val _scannedSampleLiveData = MutableLiveData<MutableList<SampleItemUIModel>>()
    val scannedSampleLiveData: LiveData<MutableList<SampleItemUIModel>>
        get() = _scannedSampleLiveData

    private var _scanProductCodeLive = MutableLiveData<Resource<ProductIdWithTypeDomain>>()
    val scanProductCodeLive: LiveData<Resource<ProductIdWithTypeDomain>>
        get() = _scanProductCodeLive


    private val _saveSampleLiveData = MutableLiveData<Resource<String>>()
    val saveSampleLiveData: LiveData<Resource<String>>
        get() = _saveSampleLiveData

    private val _voucherScanLiveData = MutableLiveData<Resource<VoucherScanDomain>>()
    val voucherScanLiveData: LiveData<Resource<VoucherScanDomain>>
        get() = _voucherScanLiveData

    private var _returnSampleLiveData = MutableLiveData<Resource<String>>()
    val returnSampleLiveData: LiveData<Resource<String>>
        get() = _returnSampleLiveData
    fun resetReturnSampleLiveData(){
        _returnSampleLiveData.value = null
    }

    fun resetsaveSampleLiveData() {
        _saveSampleLiveData.value = null
    }

    fun resetVoucherScanLive() {
        _voucherScanLiveData.value = null
    }

    fun resetScanProductCodeLive() {
        _scanProductCodeLive.value = null
    }

    var scannedSamples = mutableListOf<SampleItemUIModel>()
    var scannedSamplesForReturn = mutableListOf<SampleItemUIModel>()
    var specificationList = mutableListOf<String>()

    fun addStockSample(item: SampleItemUIModel) {
        scannedSamples.add(item)
            specificationList.add("")
        _scannedSampleLiveData.value = scannedSamples
    }

    fun removeSample(item: SampleItemUIModel) {
        specificationList.removeAt(scannedSamples.indexOf(item))
        scannedSamples.remove(item)
        _scannedSampleLiveData.value = scannedSamples
    }

    fun addStockSampleForReturn(item: SampleItemUIModel) {
        scannedSamplesForReturn.add(item)
        _scannedSampleForReturnLiveData.value = scannedSamplesForReturn
    }

    fun removeSampleForReturn(item: SampleItemUIModel) {
        specificationList.removeAt(scannedSamplesForReturn.indexOf(item))
        scannedSamplesForReturn.remove(item)
        _scannedSampleForReturnLiveData.value = scannedSamplesForReturn
    }

    fun resetSampleForReturn() {
        scannedSamplesForReturn = mutableListOf<SampleItemUIModel>()
        _scannedSampleForReturnLiveData.value = mutableListOf<SampleItemUIModel>()
        _sampleLiveData.value = null
    }

    fun resetSample() {
        specificationList.removeAll(specificationList)
        scannedSamples.removeAll(scannedSamples)
        _scannedSampleLiveData.value = scannedSamples
    }



    fun returnSample(sampleIdList:List<String>){
        viewModelScope.launch {
            returnSampleUseCase(
                localDatabase.getToken().orEmpty(),
                sampleIdList).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _returnSampleLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _returnSampleLiveData.value = Resource.Success(it.data!!.message)
                    }
                    is Resource.Error -> {
                        _returnSampleLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun getInventorySampleList() {
        viewModelScope.launch {
            getInventorySampleUseCase(
                localDatabase.getToken().orEmpty()).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _getInventorySampleLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _getInventorySampleLiveData.value = Resource.Success(it.data!!)
                    }
                    is Resource.Error -> {
                        _getInventorySampleLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun saveSamples() {
        viewModelScope.launch {
            val sampleIdHashMap: HashMap<String, String> = HashMap()
            if (specificationList.filter { it.isNotEmpty() }.isEmpty()){
                _saveSampleLiveData.value = Resource.Error("Please Enter Specification")
            }
            else{
                repeat(scannedSamples.filter { it.specification.isNullOrEmpty() }.map { it.id }.size) {
                    sampleIdHashMap["sample[$scannedProductCode]"] =
                        specificationList.filter { it.isNotEmpty() }[it]
                }

                saveSampleUseCase(localDatabase.getToken().orEmpty(), sampleIdHashMap).collectLatest {
                    when (it) {
                        is Resource.Loading -> {
                            _saveSampleLiveData.value = Resource.Loading()
                        }
                        is Resource.Success -> {
                            _saveSampleLiveData.value = Resource.Success(it.data!!.message)
                            scannedSamples.filter { it.specification.isNullOrEmpty() }.forEach { sampleItemUIModel ->
                                specificationList.filter { it.isNotEmpty() }.forEach {
                                    sampleItemUIModel.specification = it
                                }
                            }
//                            repeat(scannedSamples.filter { it.specification.isNullOrEmpty() }
//                            .size) {
//                              scannedSamples[it].specification = specificationList.filter { it.isNotEmpty() }[it]
//                        }
                            _scannedSampleLiveData.value = scannedSamples
                        }
                        is Resource.Error -> {
                            _saveSampleLiveData.value = Resource.Error(it.message)
                        }
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
                            Resource.Success(it.data!!.asUIModel())
                    }
                    is Resource.Error -> {
                        _sampleLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun checkSampleWithVoucher(stockId: String) {
        viewModelScope.launch {
            checkSampleWithVoucherUseCase(localDatabase.getToken().orEmpty(), stockId).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _sampleLiveDataFromVoucher.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _sampleLiveDataFromVoucher.value =
                            Resource.Success(it.data!!.map { it.asUIModel() })
                    }
                    is Resource.Error -> {
                        _sampleLiveDataFromVoucher.value = Resource.Error(it.message)
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
//                        checkSampleUseCase(localDatabase.getToken().orEmpty(), it.data!!.id)
                    }
                    is Resource.Error -> {
                        _voucherScanLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

}