package com.critx.shwemiAdmin.screens.setupStock.fourth.edit

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.SetupStock.jewelleryCategory.CalculateKPY
import com.critx.domain.useCase.SetUpStock.CalculateKPYUseCase
import com.critx.domain.useCase.SetUpStock.CreateJewelleryCategoryUseCase
import com.critx.domain.useCase.SetUpStock.CreateJewelleryGroupUseCase
import com.critx.domain.useCase.SetUpStock.GetDesignListUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.setupStock.DesignUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
import com.critx.shwemiAdmin.uistate.DesignUiState
import com.critx.shwemiAdmin.uistate.JewelleryCategoryUiState
import com.critx.shwemiAdmin.uistate.JewelleryGroupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val createJewelleryCategoryUseCase: CreateJewelleryCategoryUseCase,
    private val calculateKPYUseCase: CalculateKPYUseCase,
    private val getDesignListUseCase: GetDesignListUseCase,
    private val localDatabase: LocalDatabase
):ViewModel() {
    var selectedImgUri1: SelectedImage? = null
    var selectedImgUri2: SelectedImage? = null
    var selectedImgUri3: SelectedImage? = null
    var selectedVideoUri: File? = null
    var selectedGifUri: File? = null
    var calculatedKPYtoGram: Double? = null
    var selectedDesignIds:MutableList<Int>? = null

    private val _createJewelleryCategory = MutableStateFlow(JewelleryCategoryUiState())
    val createJewelleryCategoryState = _createJewelleryCategory.asStateFlow()

    private val _getDesign = MutableStateFlow(DesignUiState())
    val getDesign = _getDesign.asStateFlow()


    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()


    fun createJewelleryCategory(
        jewellery_type_id : RequestBody,
        jewellery_quality_id : RequestBody,
        groupId:RequestBody,
        is_frequently_used : RequestBody,
        name : RequestBody,
        avgWeigh:RequestBody,
        images:MutableList<MultipartBody.Part>,
        video:MultipartBody.Part,
        specification:RequestBody,
        design:MutableList<RequestBody>,
        orderToGs:RequestBody,
        kyat:Double,pae:Double,ywae:Double
    ){
        viewModelScope.launch {
            //calculate kyp
            calculateKPYUseCase(localDatabase.getToken().orEmpty(),kyat,pae,ywae).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _createJewelleryCategory.value =_createJewelleryCategory.value.copy(
                            calculateKPYLoading = true
                        )
                    }
                    is Resource.Success->{
                        _createJewelleryCategory.value =_createJewelleryCategory.value.copy(
                            calculateKPYLoading = false,
                            calculateKPYSuccessLoading = it.data?.gram?:0.0
                        )
                        val avgWastage = it.data?.gram.toString()
                            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                        createJewelleryCategoryUseCase(
                            localDatabase.getToken().orEmpty(),
                            jewellery_type_id, jewellery_quality_id, groupId, is_frequently_used, name, avgWeigh, avgWastage, images, video, specification, design,orderToGs
                        ).collectLatest {
                            when(it){
                                is Resource.Loading->{
                                    _createJewelleryCategory.value =_createJewelleryCategory.value.copy(
                                        createLoading = true
                                    )
                                }
                                is Resource.Success->{
                                    _createJewelleryCategory.value =_createJewelleryCategory.value.copy(
                                        createLoading = false,
                                        createSuccessLoading = "Category Created"
                                    )
                                }
                                is Resource.Error->{
                                    _createJewelleryCategory.value =_createJewelleryCategory.value.copy(
                                        createLoading = false,
                                    )
                                    it.message?.let {errorString->
                                        _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                                    }
                                }
                            }
                        }
                    }
                    is Resource.Error->{
                        _createJewelleryCategory.value =_createJewelleryCategory.value.copy(
                            calculateKPYLoading = false,
                        )
                        it.message?.let {errorString->
                            _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                        }
                    }
                }
            }
        }
    }

    fun getDesign(){
        viewModelScope.launch {
            getDesignListUseCase(localDatabase.getToken().orEmpty()).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _getDesign.value =_getDesign.value.copy(
                            loading = true
                        )
                    }
                    is Resource.Success->{
                        _getDesign.value =_getDesign.value.copy(
                            loading = false,
                            success = it.data!!.map { it.asUiModel() }
                        )
                    }
                    is Resource.Error->{
                        _getDesign.value =_getDesign.value.copy(
                            loading = false,
                        )
                        it.message?.let {errorString->
                            _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                        }
                    }
                }
            }
        }
    }


}

data class SelectedImage(
    val file:File,
    val bitMap:Bitmap
)