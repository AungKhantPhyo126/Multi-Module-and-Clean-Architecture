package com.critx.shwemiAdmin.screens.editStock

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.data.network.dto.setupStock.ProductSingleDto
import com.critx.domain.model.SetupStock.ProductSingleDomain
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
import com.critx.domain.useCase.SetUpStock.EditProductUseCase
import com.critx.domain.useCase.SetUpStock.GetProductCodeUseCase
import com.critx.domain.useCase.SetUpStock.GetProductSingleUseCase
import com.critx.domain.useCase.collectStock.ScanProductCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditStockViewModel @Inject constructor(
    private val getProductSingleUseCase: GetProductSingleUseCase,
    private val localDatabase: LocalDatabase,
    private val scanProductCodeUseCase: ScanProductCodeUseCase,
    private val editProductUseCase: EditProductUseCase
) : ViewModel() {
    var selectedImgUri1: File? = null
    var selectedImgUri2: File? = null
    var selectedImgUri3: File? = null
    var selectedGifUri: File? = null
    var selectedVideoUri: File? = null

    var diamondInfo: String? = null
    var diamondPriceFromGS: Int? = null
    var diamondValueFromGS: Int? = null
    var diamondPriceForSale: Int? = null
    var diamondValueForSale: Int? = null
    var gemValue: String? = null


    fun resetDimaondData() {
        diamondInfo = null
        diamondPriceFromGS = null
        diamondValueFromGS = null
        diamondPriceForSale = null
        diamondValueForSale = null
    }

    private val _getProductLiveData = MutableLiveData<Resource<ProductSingleDomain>>()
    val getProductLiveData: LiveData<Resource<ProductSingleDomain>>
        get() = _getProductLiveData

    fun getProduct(productCode: String) {
        _getProductLiveData.value = Resource.Loading()
        viewModelScope.launch {
            getProductSingleUseCase(localDatabase.getToken().orEmpty(), productCode).collectLatest {
                _getProductLiveData.value = it
            }
        }
    }


    private var _scanProductCodeLive = MutableLiveData<Resource<ProductIdWithTypeDomain>>()
    val scanProductCodeLive: LiveData<Resource<ProductIdWithTypeDomain>>
        get() = _scanProductCodeLive

    fun resetScanProductCodeLive() {
        _scanProductCodeLive.value = null
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

    private val _editProductLiveData = MutableLiveData<Resource<String>>()
    val editProductLiveData: LiveData<Resource<String>>
        get() = _editProductLiveData

    fun resetEditProductLiveData() {
        _editProductLiveData.value = null
    }

    fun editProduct(
        productCode: String,
        name: RequestBody?,
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
        val method = "PUT"
        val methodRequestBody = method.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        _editProductLiveData.value = Resource.Loading()
        viewModelScope.launch {
            val result = editProductUseCase(
                localDatabase.getToken().orEmpty(),
                methodRequestBody,
                productCode,
                name,
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
                image1, image1Id, image2, image2Id, image3, image3Id, gif, gifId, video
            )
                when (result) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        _editProductLiveData.value = Resource.Success(result.data!!.message)

                    }
                    is Resource.Error -> {
                        _editProductLiveData.value = Resource.Error(result.message)
                    }
            }
        }
    }

}