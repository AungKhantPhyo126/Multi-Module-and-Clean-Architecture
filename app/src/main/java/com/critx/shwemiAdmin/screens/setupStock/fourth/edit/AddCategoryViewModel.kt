package com.critx.shwemiAdmin.screens.setupStock.fourth.edit

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.SetupStock.jewelleryCategory.CalculateKPY
import com.critx.domain.useCase.SetUpStock.*
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
    private val editJewelleryCategoryUseCase: EditJewelleryCategoryUseCase,
    private val calculateKPYUseCase: CalculateKPYUseCase,
    private val getDesignListUseCase: GetDesignListUseCase,
    private val getRelatedCategoryUseCase: GetRelatedCategoryUseCase,
    private val localDatabase: LocalDatabase
) : ViewModel() {
    var selectedImgUri1 = MutableLiveData<SelectedImage?>(null)
    var selectedImgUri2 = MutableLiveData<SelectedImage?>(null)
    var selectedImgUri3 = MutableLiveData<SelectedImage?>(null)
    var selectedVideoUri = MutableLiveData<File?>(null)
    var selectedGifUri = MutableLiveData<SelectedImage?>(null)
    var calculatedKPYtoGram: Double? = null
    var selectedDesignIds: MutableList<Int>? = null
    var selectedRecommendCat = MutableLiveData<List<Int>?>(null)

    fun setSelectedImgUri1(selectedImage: SelectedImage?) {
        selectedImgUri1.value = selectedImage
    }

    fun setSelectedImgUri2(selectedImage: SelectedImage?) {
        selectedImgUri2?.value = selectedImage
    }

    fun setSelectedImgUri3(selectedImage: SelectedImage?) {
        selectedImgUri3?.value = selectedImage
    }

    fun setSelectedGif(selectedImage: SelectedImage?) {
        selectedGifUri?.value = selectedImage
    }

    fun setSelectedVideo(selectedVideo: File?) {
        selectedVideoUri?.value = selectedVideo
    }

    fun setSelectedRecommendCat(selectedCats: List<Int>?) {
        selectedRecommendCat?.value = selectedCats
    }

    private val _createJewelleryCategory = MutableStateFlow(JewelleryCategoryUiState())
    val createJewelleryCategoryState = _createJewelleryCategory.asStateFlow()


    private val _getDesign = MutableStateFlow(DesignUiState())
    val getDesign = _getDesign.asStateFlow()


    private val _getRelatedCats = MutableStateFlow(JewelleryCategoryUiState())
    val getRelatedCats = _getRelatedCats.asStateFlow()

    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    private val _editJewelleryCategoryState = MutableStateFlow(JewelleryCategoryUiState())
    val editJewelleryCategoryState = _editJewelleryCategoryState.asStateFlow()

    fun getRelatedCat(id:String){
        viewModelScope.launch {
            getRelatedCategoryUseCase(localDatabase.getToken().orEmpty(),id).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getRelatedCats.value = _getRelatedCats.value.copy(
                            getRelatedCatsLoading = true
                        )
                    }
                    is Resource.Success -> {
                        _getRelatedCats.update { uiState ->

                            uiState.copy(
                                getRelatedCatsLoading = false,
                                getRelatedCatsSuccessLoading = result.data!!.map { it.asUiModel() }
                            )
                        }

//                        result.data?.forEach {
//                            selectImage(it.id)
//                        }
                    }
                    is Resource.Error -> {
                        _getRelatedCats.value = _getRelatedCats.value.copy(
                            getRelatedCatsLoading = false,
                        )
                        result.message?.let { errorString ->
                            _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                        }
                    }
                }

            }
        }
    }


    fun createJewelleryCategory(
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        groupId: RequestBody,
        is_frequently_used: RequestBody,
        withGem: RequestBody,
        name: RequestBody,
        avgWeigh: RequestBody,
        images: MutableList<MultipartBody.Part>,
        video: MultipartBody.Part?,
        specification: RequestBody,
        design: MutableList<RequestBody>,
        orderToGs: RequestBody,
        avgKyat: RequestBody,
        avgPae: RequestBody,
        avgYwae: RequestBody,
        recommendCat: MutableList<RequestBody>?

    ) {
        viewModelScope.launch {
            //calculate kyp
            createJewelleryCategoryUseCase(
                localDatabase.getToken().orEmpty(),
                jewellery_type_id,
                jewellery_quality_id,
                groupId,
                is_frequently_used,
                withGem,
                name,
                avgWeigh,
                avgKyat,
                avgPae,
                avgYwae,
                images,
                video,
                specification,
                design,
                orderToGs,
                recommendCat
            ).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _createJewelleryCategory.value = _createJewelleryCategory.value.copy(
                            createLoading = true
                        )
                    }
                    is Resource.Success -> {
                        _createJewelleryCategory.value = _createJewelleryCategory.value.copy(
                            createLoading = false,
                            createSuccessLoading = it.data!!.asUiModel()
                        )
                    }
                    is Resource.Error -> {
                        _createJewelleryCategory.value = _createJewelleryCategory.value.copy(
                            createLoading = false
                        )
                        it.message?.let { errorString ->
                            _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                        }
                    }
                }
            }
        }

    }

    fun editJewelleryCategory(
        categoryId: String,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        groupId: RequestBody,
        is_frequently_used: RequestBody,
        withGem:RequestBody,
        name: RequestBody,
        avgWeigh: RequestBody,
        images: MutableList<MultipartBody.Part>,
        video: MultipartBody.Part?,
        specification: RequestBody,
        design: MutableList<RequestBody>,
        orderToGs: RequestBody,
        avgKyat: RequestBody,
        avgPae: RequestBody,
        avgYwae: RequestBody,
        recommendCat: MutableList<RequestBody>
    ) {
        val method = "PUT"
        val methodRequestBody = method.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        viewModelScope.launch {
            //calculate kyp
            editJewelleryCategoryUseCase(
                localDatabase.getToken().orEmpty(),
                methodRequestBody,
                categoryId,
                jewellery_type_id,
                jewellery_quality_id,
                groupId,
                is_frequently_used,
                withGem,
                name,
                avgWeigh,
                avgKyat,
                avgPae,
                avgYwae,
                images,
                video,
                specification,
                design,
                orderToGs,
                recommendCat
            ).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _editJewelleryCategoryState.value =
                            _editJewelleryCategoryState.value.copy(
                                editLoading = true
                            )
                    }
                    is Resource.Success -> {
                        _editJewelleryCategoryState.value =
                            _editJewelleryCategoryState.value.copy(
                                editLoading = false,
                                editSuccessLoading = "Category Updated"
                            )
                    }
                    is Resource.Error -> {
                        _editJewelleryCategoryState.value =
                            _editJewelleryCategoryState.value.copy(
                                editLoading = false
                            )
                        result.message?.let { errorString ->
                            _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                        }
                    }
                }
            }
        }


    }


    fun getDesign(jewelleryType:String) {
        viewModelScope.launch {
            getDesignListUseCase(localDatabase.getToken().orEmpty(),jewelleryType).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _getDesign.value = _getDesign.value.copy(
                            loading = true
                        )
                    }
                    is Resource.Success -> {
                        _getDesign.value = _getDesign.value.copy(
                            loading = false,
                            success = it.data!!.map { it.asUiModel() }
                        )
                    }
                    is Resource.Error -> {
                        _getDesign.value = _getDesign.value.copy(
                            loading = false,
                        )
                        it.message?.let { errorString ->
                            _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                        }
                    }
                }
            }
        }
    }


}

data class SelectedImage(
    val file: File,
    val bitMap: Bitmap
)