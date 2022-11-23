package com.critx.shwemiAdmin.screens.orderStock.fillOrderInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
import com.critx.domain.model.orderStock.BookMarkStockInfoDomain
import com.critx.domain.useCase.collectStock.GetGoldSmithListUseCase
import com.critx.domain.useCase.collectStock.GetJewellerySizeUseCase
import com.critx.domain.useCase.collectStock.ScanProductCodeUseCase
import com.critx.domain.useCase.orderStock.GetBookMarkStockInfoUseCase
import com.critx.domain.useCase.orderStock.OrderStockUseCase
import com.critx.domain.useCase.sampleTakeAndReturn.CheckSampleUseCase
import com.critx.domain.useCase.sampleTakeAndReturn.SaveOutsideSampleUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.SelectedImage
import com.critx.shwemiAdmin.uiModel.collectStock.GoldSmithUiModel
import com.critx.shwemiAdmin.uiModel.collectStock.JewellerySizeUIModel
import com.critx.shwemiAdmin.uiModel.collectStock.asUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryTypeUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.asUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class FillOrderInfoViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getGoldSmithListUseCase: GetGoldSmithListUseCase,
    private val getBookMarkStockInfoUseCase: GetBookMarkStockInfoUseCase,
    private val orderStockUseCase: OrderStockUseCase,
    private val saveOutsideSampleUseCase: SaveOutsideSampleUseCase,
    private val checkSampleUseCase: CheckSampleUseCase,
    private val scanProductCodeUseCase: ScanProductCodeUseCase,
    private val getJewellerySizeUseCase: GetJewellerySizeUseCase
) : ViewModel() {
    var orderQtyList = mutableListOf<String>()
    var jewellerySizeIdList = mutableListOf<String>()

    var selectedImgUri = MutableLiveData<SelectedImage?>(null)
    fun setSelectedImgUri(selectedImage: SelectedImage?) {
        selectedImgUri.value = selectedImage
    }

    var selectedOrderedImgUri = MutableLiveData<SelectedImage?>(null)
    fun setSelectedOrderedImgUri(selectedImage: SelectedImage?) {
        selectedOrderedImgUri.value = selectedImage
    }

    fun resetSelectedImg() {
        selectedImgUri.value = null
    }

    private var _jewellerySizeLiveData= MutableLiveData<Resource<MutableList<JewellerySizeUIModel>>>()
    val jewellerySizeLiveData : LiveData<Resource<MutableList<JewellerySizeUIModel>>>
        get() = _jewellerySizeLiveData

    private val _checkSampleLiveData = MutableLiveData<Resource<SampleItemUIModel>>()
    val checkSampleLiveData: LiveData<Resource<SampleItemUIModel>>
        get() = _checkSampleLiveData

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

    private var _scanProductCodeLive = MutableLiveData<Resource<ProductIdWithTypeDomain>>()
    val scanProductCodeLive: LiveData<Resource<ProductIdWithTypeDomain>>
        get() = _scanProductCodeLive

    private var _saveOutsideSampleLiveData = MutableLiveData<Resource<SampleItemUIModel>>()
    val saveOutsideSampleLiveData: LiveData<Resource<SampleItemUIModel>>
        get() = _saveOutsideSampleLiveData

    private var _bookMarkStockInfoLiveData =
        MutableLiveData<Resource<List<BookMarkStockInfoDomain>>>()
    val bookMarkStockInfoLiveData: LiveData<Resource<List<BookMarkStockInfoDomain>>>
        get() = _bookMarkStockInfoLiveData


    private var _goldSmithListLiveData = MutableLiveData<Resource<MutableList<GoldSmithUiModel>>>()
    val goldSmithListLiveData: LiveData<Resource<MutableList<GoldSmithUiModel>>>
        get() = _goldSmithListLiveData

    private var _orderStockLiveData = MutableLiveData<Resource<String>>()
    val orderStockLiveData: LiveData<Resource<String>>
        get() = _orderStockLiveData

    fun getBookMarkStockInfo(
        bookMarkId: String
    ) {
        viewModelScope.launch {
            getBookMarkStockInfoUseCase(
                localDatabase.getToken().orEmpty(),
                bookMarkId
            ).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _bookMarkStockInfoLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _bookMarkStockInfoLiveData.value = Resource.Success(it.data!!)
                    }
                    is Resource.Error -> {
                        _bookMarkStockInfoLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

    fun getJewellerySize(type:String){
        viewModelScope.launch {
            getJewellerySizeUseCase(localDatabase.getToken().orEmpty(),
                type).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _jewellerySizeLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _jewellerySizeLiveData.value = Resource.Success(it.data?.map { it.asUiModel() } as MutableList<JewellerySizeUIModel>)
                    }
                    is Resource.Error->{
                        _jewellerySizeLiveData.value = Resource.Error(it.message)
                    }
                }
            }

        }
    }

    fun getGoldSmithList() {
        viewModelScope.launch {
            getGoldSmithListUseCase(localDatabase.getToken().orEmpty()).collectLatest {
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


    fun checkSampleUseCase(stockId: String) {
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

    fun orderStock(
        bookMarkAvgKyat: MultipartBody.Part?,
        bookMarkAvgPae: MultipartBody.Part?,
        bookMarkAvgYwae: MultipartBody.Part?,
        bookMarkJewelleryTypeId: MultipartBody.Part?,
        bookMarkImage: MultipartBody.Part?,
        goldQuality: MultipartBody.Part,
        goldSmith: MultipartBody.Part,
        bookMarkId: MultipartBody.Part?,
        equivalent_pure_gold_weight_kpy: MultipartBody.Part,
        jewellery_type_size_id: List<MultipartBody.Part>,
        order_qty: List<MultipartBody.Part>,
        sample_id: List<MultipartBody.Part>?
    ) {
        viewModelScope.launch {
            orderStockUseCase(
                localDatabase.getToken().orEmpty(),
                bookMarkAvgKyat,
                bookMarkAvgPae,
                bookMarkAvgYwae,
                bookMarkJewelleryTypeId,
                bookMarkImage,
                goldQuality,
                goldSmith,
                bookMarkId,
                equivalent_pure_gold_weight_kpy,
                jewellery_type_size_id,
                order_qty,
                sample_id
            ).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _orderStockLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _orderStockLiveData.value = Resource.Success(it.data!!.message)
                    }
                    is Resource.Error -> {
                        _orderStockLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }

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
}