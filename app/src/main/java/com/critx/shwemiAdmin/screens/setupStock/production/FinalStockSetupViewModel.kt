package com.critx.shwemiAdmin.screens.setupStock.production

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.domain.useCase.SetUpStock.CreateProductUseCase
import com.critx.domain.useCase.SetUpStock.GetProductCodeUseCase
import com.critx.shwemiAdmin.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FinalStockSetupViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val createProductUseCase: CreateProductUseCase,
    private val getProductCodeUseCase: GetProductCodeUseCase
) : ViewModel() {

    var selectedImgUri1: File? = null
    var selectedImgUri2: File? = null
    var selectedImgUri3: File? = null
    var selectedGifUri: File? = null
    var selectedVideoUri: File? = null


    var diamondInfo: String? = null
    var diamondPriceFromGS: String? = null
    var diamondValueFromGS: String? = null
    var diamondPriceForSale: String? = null
    var diamondValueForSale: String? = null
    var gemValue: String? = null

    fun resetDimaondData() {
        diamondInfo = null
        diamondPriceFromGS = null
        diamondValueFromGS = null
        diamondPriceForSale = null
        diamondValueForSale = null
    }

    private val _createProductLiveData = MutableLiveData<Resource<String>>()
    val createProductLiveData: LiveData<Resource<String>>
        get() = _createProductLiveData

    fun resetCreateLiveData() {
        _createProductLiveData.value = null
    }

    private val _getProductLiveData = MutableLiveData<Resource<String>>()
    val getProductLiveData: LiveData<Resource<String>>
        get() = _getProductLiveData

    fun resetGetProductLiveData() {
        _getProductLiveData.value = null
    }

    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun createProduct(
        name: RequestBody?,
        productCode: RequestBody,
        type: RequestBody,
        quality: RequestBody,
        group: RequestBody?,
        categoryId: RequestBody?,
        goldAndGemWeight: RequestBody?,
        gemWeightYwae: RequestBody?,
        gemValue: RequestBody?,
        ptAndClipCost: RequestBody?,
        maintenanceCost: RequestBody?,
        diamondInfo: RequestBody?,
        diamondPriceFromGS: RequestBody?,
        diamondValueFromGS: RequestBody?,
        diamondPriceForSale: RequestBody?,
        diamondValueForSale: RequestBody?,
        image1: MultipartBody.Part?,
        image1Id: MultipartBody.Part?,
        image2: MultipartBody.Part?,
        image2Id: MultipartBody.Part?,
        image3: MultipartBody.Part?,
        image3Id: MultipartBody.Part?,
        gif: MultipartBody.Part?,
        gifId: MultipartBody.Part?,
        video: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            createProductUseCase(
                localDatabase.getToken().orEmpty(),
                name,
                productCode,
                type,
                quality,
                group,
                categoryId,
                goldAndGemWeight,
                gemWeightYwae,
                gemValue,
                ptAndClipCost,
                maintenanceCost,
                diamondInfo,
                diamondPriceFromGS,
                diamondValueFromGS,
                diamondPriceForSale,
                diamondValueForSale,
                image1,
                image1Id,
                image2,
                image2Id,
                image3,
                image3Id,
                gif,
                gifId,
                video
            ).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _createProductLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _createProductLiveData.value = Resource.Success(it.data!!.message)
                    }
                    is Resource.Error -> {
                        _createProductLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }


    fun getProductCode(jewelleryQualityId:String) {
        viewModelScope.launch {
            getProductCodeUseCase(localDatabase.getToken().orEmpty(),jewelleryQualityId).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _getProductLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _getProductLiveData.value = Resource.Success(it.data!!.code)
                    }
                    is Resource.Error -> {
                        _getProductLiveData.value = Resource.Error(it.message)
                    }
                }
            }
        }
    }
}