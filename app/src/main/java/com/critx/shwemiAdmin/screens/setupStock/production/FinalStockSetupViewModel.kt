package com.critx.shwemiAdmin.screens.setupStock.production

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.useCase.SetUpStock.CreateProductUseCase
import com.critx.domain.useCase.SetUpStock.GetProductCodeUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.SelectedImage
import com.critx.shwemiAdmin.uistate.CreateProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
) :ViewModel(){

    var selectedImgUri1: SelectedImage? = null
    var selectedImgUri2: SelectedImage? = null
    var selectedImgUri3: SelectedImage? = null
    var selectedVideoUri: File? = null
    var selectedGifUri: SelectedImage? = null

    var diamondInfo:String? = null
    var diamondPriceFromGS:String? = null
    var diamondValueFromGS:String? = null
    var diamondPriceForSale:String? = null
    var diamondValueForSale:String? = null
    var gemValue:String? = null

    fun resetDimaondData(){
         diamondInfo = null
         diamondPriceFromGS = null
         diamondValueFromGS = null
         diamondPriceForSale = null
         diamondValueForSale = null
    }

    private val _createProductUiState = MutableStateFlow(CreateProductUiState())
    val createProductState = _createProductUiState.asStateFlow()


    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun createProduct(
        name: RequestBody,
        productCode:RequestBody,
        type: RequestBody,
        quality: RequestBody,
        group: RequestBody,
        categoryId: RequestBody,
        goldAndGemWeight: RequestBody,
        gemWeightKyat: RequestBody,
        gemWeightPae: RequestBody,
        gemWeightYwae: RequestBody,
        gemValue: RequestBody?,
        ptAndClipCost: RequestBody?,
        maintenanceCost: RequestBody?,
        diamondInfo: RequestBody?,
        diamondPriceFromGS: RequestBody?,
        diamondValueFromGS: RequestBody?,
        diamondPriceForSale: RequestBody?,
        diamondValueForSale: RequestBody?,
        images: List<MultipartBody.Part>,
        video: MultipartBody.Part
    ){
        viewModelScope.launch {
            createProductUseCase(localDatabase.getToken().orEmpty(),
            name,productCode, type, quality, group, categoryId, goldAndGemWeight,
                gemWeightKyat, gemWeightPae, gemWeightYwae, gemValue,
                ptAndClipCost, maintenanceCost, diamondInfo, diamondPriceFromGS,
                diamondValueFromGS, diamondPriceForSale, diamondValueForSale, images, video).collectLatest {
                    when(it){
                        is Resource.Loading->{
                            _createProductUiState.update { uiState->
                                uiState.copy(loading = true)
                            }
                        }
                        is Resource.Success->{
                            _createProductUiState.update { uiState->
                                uiState.copy(loading = false, success = "success")
                            }
                        }
                        is Resource.Error->{
                            _createProductUiState.update { uiState->
                                uiState.copy(loading = false, error = "success", success = null)
                            }
                            it.message?.let {errorString->
                                _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                            }
                        }
                    }
            }
        }
    }

    init {
        getProductCode()
    }
    fun getProductCode(){
        viewModelScope.launch {
            getProductCodeUseCase(localDatabase.getToken().orEmpty()).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _createProductUiState.update { uiState->
                            uiState.copy(getProductCodeLoading = true)
                        }
                    }
                    is Resource.Success->{
                        _createProductUiState.update { uiState->
                            uiState.copy(getProductCodeLoading = false, getProductCodeSuccess = it.data?.code)
                        }
                    }
                    is Resource.Error->{
                        _createProductUiState.update { uiState->
                            uiState.copy(getProductCodeLoading = false, getProductCodeError = it.message, getProductCodeSuccess = null)
                        }
                        it.message?.let {errorString->
                            _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                        }
                    }
                }
            }
        }
    }

}