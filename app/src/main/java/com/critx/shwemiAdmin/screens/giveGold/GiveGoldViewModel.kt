package com.critx.shwemiAdmin.screens.giveGold

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
import com.critx.domain.model.giveGold.GoldBoxDomain
import com.critx.domain.model.repairStock.JobDoneDomain
import com.critx.domain.model.sampleTakeAndReturn.VoucherScanDomain
import com.critx.domain.useCase.collectStock.GetGoldSmithListUseCase
import com.critx.domain.useCase.collectStock.ScanProductCodeUseCase
import com.critx.domain.useCase.giveGold.GetGoldBoxIdUseCase
import com.critx.domain.useCase.giveGold.GiveGoldScanUseCase
import com.critx.domain.useCase.giveGold.GiveGoldUseCase
import com.critx.domain.useCase.giveGold.ServiceChargeUseCase
import com.critx.domain.useCase.sampleTakeAndReturn.CheckSampleUseCase
import com.critx.domain.useCase.sampleTakeAndReturn.CheckSampleWithVoucherUseCase
import com.critx.domain.useCase.sampleTakeAndReturn.SaveOutsideSampleUseCase
import com.critx.domain.useCase.sampleTakeAndReturn.ScanInvoiceUseCase
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.SelectedImage
import com.critx.shwemiAdmin.uiModel.collectStock.GoldSmithUiModel
import com.critx.shwemiAdmin.uiModel.collectStock.asUiModel
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.asUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class GiveGoldViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val giveGoldUseCase: GiveGoldUseCase,
    private val getGoldSmithListUseCase: GetGoldSmithListUseCase,
    private val getGoldBoxIdUseCase: GetGoldBoxIdUseCase,
    private val giveGoldScanUseCase: GiveGoldScanUseCase,
    private val serviceChargeUseCase: ServiceChargeUseCase,
    private val scanProductCodeUseCase: ScanProductCodeUseCase,
    private val checkSampleUseCase: CheckSampleUseCase,
    private val saveOutsideSampleUseCase: SaveOutsideSampleUseCase,
    private val scanInvoiceUseCase: ScanInvoiceUseCase,
    private val checkSampleWithVoucherUseCase: CheckSampleWithVoucherUseCase,

    ) : ViewModel() {
    var selectedGSId: String? = null
    var selectedGoldBoxId: String? = null

    private var _takenSampleListLiveData = MutableLiveData<List<SampleItemUIModel>>()
    val takenSampleListLiveData: LiveData<List<SampleItemUIModel>>
        get() = _takenSampleListLiveData

    val sampleList = mutableListOf<SampleItemUIModel>()

    fun addSample(item: SampleItemUIModel) {
        sampleList.add(item)
        _takenSampleListLiveData.value = sampleList
    }

    fun remove(item: SampleItemUIModel) {
        sampleList.remove(item)
        _takenSampleListLiveData.value = sampleList
    }

    fun resetTakenSamplelist() {
        sampleList.removeAll(sampleList)
        _takenSampleListLiveData.value = sampleList

    }

    private var _serviceChargeLiveData = MutableLiveData<Resource<String>>()
    val serviceChargeLiveData: LiveData<Resource<String>>
        get() = _serviceChargeLiveData

    fun resetserviceChargeLiveData() {
        _serviceChargeLiveData.value = null
    }

    private var _giveGoldLiveData = MutableLiveData<Resource<String>>()
    val giveGoldLiveData: LiveData<Resource<String>>
        get() = _giveGoldLiveData

    fun resetGiveGoldLiveData() {
        _giveGoldLiveData.value = null
    }

    private var _scanProductCodeLive = MutableLiveData<Resource<ProductIdWithTypeDomain>>()
    val scanProductCodeLive: LiveData<Resource<ProductIdWithTypeDomain>>
        get() = _scanProductCodeLive

    private var _getGoldBoxLiveData = MutableLiveData<Resource<List<GoldBoxDomain>>>()
    val getGoldBoxLiveData: LiveData<Resource<List<GoldBoxDomain>>>
        get() = _getGoldBoxLiveData


    private var _goldSmithListLiveData = MutableLiveData<Resource<MutableList<GoldSmithUiModel>>>()
    val goldSmithListLiveData: LiveData<Resource<MutableList<GoldSmithUiModel>>>
        get() = _goldSmithListLiveData

    fun serviceCharge(invoiceNumber: String, wastageGm: String, chargeAmount: String) {
        viewModelScope.launch {
            giveGoldScanUseCase(localDatabase.getToken().orEmpty(), invoiceNumber).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _serviceChargeLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        serviceChargeUseCase(
                            localDatabase.getToken().orEmpty(), chargeAmount, wastageGm,
                            it.data!!.id
                        ).collectLatest {
                            when (it) {
                                is Resource.Loading -> {
                                    _serviceChargeLiveData.value = Resource.Loading()

                                }
                                is Resource.Success -> {
                                    _serviceChargeLiveData.value =
                                        Resource.Success(it.data!!.message)

                                }
                                is Resource.Error -> {
                                    _serviceChargeLiveData.value = Resource.Error(it.message)

                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        _serviceChargeLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    var selectedImgUri = MutableLiveData<SelectedImage?>(null)
    fun setSelectedImgUri(selectedImage: SelectedImage?) {
        selectedImgUri.value = selectedImage
    }

    private var _saveOutsideSampleLiveData = MutableLiveData<Resource<SampleItemUIModel>>()
    val saveOutsideSampleLiveData: LiveData<Resource<SampleItemUIModel>>
        get() = _saveOutsideSampleLiveData

    fun saveOutsideSample(
        name: RequestBody?,
        weight: RequestBody?,
        specification: RequestBody?,
        image: MultipartBody.Part
    ) {
        viewModelScope.launch {
            saveOutsideSampleUseCase(
                localDatabase.getToken().orEmpty(),
                name, weight, specification, image
            ).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _saveOutsideSampleLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _saveOutsideSampleLiveData.value = Resource.Success(it.data!!.asUIModel())

                    }
                    is Resource.Error -> {
                        _saveOutsideSampleLiveData.value = Resource.Error(it.message)
                    }
                }

            }
        }
    }

    private val _checkSampleLiveData = MutableLiveData<Resource<SampleItemUIModel>>()
    val checkSampleLiveData: LiveData<Resource<SampleItemUIModel>>
        get() = _checkSampleLiveData

    fun checkSample(stockId: String) {
        viewModelScope.launch {
            checkSampleUseCase(localDatabase.getToken().orEmpty(), stockId).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _checkSampleLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _checkSampleLiveData.value =
                            Resource.Success(it.data!!.asUIModel())
                    }
                    is Resource.Error -> {
                        _checkSampleLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun getGoldSmithList() {
        viewModelScope.launch {
            getGoldSmithListUseCase(localDatabase.getToken().orEmpty(), "in").collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _goldSmithListLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _goldSmithListLiveData.value =
                            Resource.Success(it.data!!.map { it.asUiModel() } as MutableList<GoldSmithUiModel>)
                    }
                    is Resource.Error -> {
                        _goldSmithListLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun resetScanProductCodeLive() {
        _scanProductCodeLive.value = null
    }

    fun getGoldBoxId() {
        viewModelScope.launch {
            getGoldBoxIdUseCase(localDatabase.getToken().orEmpty()).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _getGoldBoxLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _getGoldBoxLiveData.value = Resource.Success(it.data!!)
                    }
                    is Resource.Error -> {
                        _getGoldBoxLiveData.value = Resource.Error(it.message)
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

    fun giveGold(
        goldSmithId: String,
        orderItem: String,
        orderQty: String,
        weightY: String,
        goldBoxId: String,
        goldWeight: String,
        gemWeight: String,
        goldAndGemWeight: String,
        wastageY: String,
        dueDate: String?,
        sampleList: List<String>?
    ) {
        viewModelScope.launch {
            giveGoldUseCase(
                localDatabase.getToken().orEmpty(),
                goldSmithId,
                orderItem,
                orderQty,
                weightY,
                goldBoxId,
                goldWeight,
                gemWeight,
                goldAndGemWeight,
                wastageY,
                dueDate,
                sampleList
            ).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _giveGoldLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _giveGoldLiveData.value = Resource.Success(it.data!!.message)
                    }
                    is Resource.Error -> {
                        _giveGoldLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    private val _voucherScanLiveData = MutableLiveData<Resource<VoucherScanDomain>>()
    val voucherScanLiveData: LiveData<Resource<VoucherScanDomain>>
        get() = _voucherScanLiveData

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

    fun resetVoucherScanLive() {
        _voucherScanLiveData.value = null
    }

    private val _sampleLiveDataFromVoucher = MutableLiveData<Resource<List<SampleItemUIModel>>>()
    val sampleLiveDataFromVoucher: LiveData<Resource<List<SampleItemUIModel>>>
        get() = _sampleLiveDataFromVoucher

    fun selectSample(sampleId: String,check:Boolean) {
        _sampleLiveDataFromVoucher.value!!.data!!.filterNotNull().find {
            it.id == sampleId
        }?.isChecked = check
        _sampleLiveDataFromVoucher.value = _sampleLiveDataFromVoucher.value


    }

    fun checkSampleWithVoucher(stockId: String) {
        viewModelScope.launch {
            checkSampleWithVoucherUseCase(
                localDatabase.getToken().orEmpty(),
                stockId
            ).collectLatest {
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

    fun resetSampleLiveDataFromVoucher() {
        _sampleLiveDataFromVoucher.value = null
    }


}