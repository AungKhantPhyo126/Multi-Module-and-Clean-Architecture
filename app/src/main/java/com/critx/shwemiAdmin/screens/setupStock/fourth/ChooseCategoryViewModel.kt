package com.critx.shwemiAdmin.screens.setupStock.third

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.SetupStock.jewelleryCategory.JewelleryCategory
import com.critx.domain.useCase.SetUpStock.CreateJewelleryGroupUseCase
import com.critx.domain.useCase.SetUpStock.DeleteJewelleryCategoryUseCase
import com.critx.domain.useCase.SetUpStock.GetJewelleryCategoryUseCase
import com.critx.domain.useCase.SetUpStock.GetJewelleryGroupUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryCategoryUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
import com.critx.shwemiAdmin.uistate.JewelleryCategoryUiState
import com.critx.shwemiAdmin.uistate.JewelleryGroupUiState
import com.critx.shwemiAdmin.uistate.JewelleryQualityUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class ChooseCategoryViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getJewelleryCategoryUseCase: GetJewelleryCategoryUseCase,
    private val deleteJewelleryCategoryUseCase: DeleteJewelleryCategoryUseCase
) : ViewModel() {
    //forselection

    var selectedJewelleryCategory = MutableLiveData<JewelleryCategoryUiModel?>(null)
    val _selectedJewelleryCategory:LiveData<JewelleryCategoryUiModel?>
    get() = selectedJewelleryCategory

    fun setSelectedCategory(item:JewelleryCategoryUiModel?){
        selectedJewelleryCategory.value = item
    }

    private val _getJewelleryCategory = MutableStateFlow(JewelleryCategoryUiState())
    val getJewelleryCategory = _getJewelleryCategory.asStateFlow()

    private val _deleteCategoryState = MutableStateFlow(JewelleryCategoryUiState())
    val deleteCategoryState = _deleteCategoryState.asStateFlow()

    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun deleteJewelleryCategory(id:String){
        val method = "DELETE"
        val methodRequestBody =method.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        viewModelScope.launch {
            deleteJewelleryCategoryUseCase(localDatabase.getToken().orEmpty(),methodRequestBody,id
            ).collectLatest {result->
                when (result) {
                    is Resource.Loading -> {
                        _deleteCategoryState.value = _deleteCategoryState.value.copy(
                            deleteLoading = true
                        )
                    }
                    is Resource.Success -> {
                        _deleteCategoryState.update { uiState ->
                            uiState.copy(
                                deleteLoading = false,
                                deleteSuccessLoading = "Successfully Deleted"
                            )

                        }


                    }
                    is Resource.Error -> {
                        _deleteCategoryState.value = _deleteCategoryState.value.copy(
                            deleteLoading = false,
                        )
                        result.message?.let { errorString ->
                            _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                        }
                    }
                }
            }
        }
    }

    fun getJewelleryCategory(isFrequentlyUse: Int,firstCatId:Int,secondCatId:Int,thirdCatId:Int) {
        viewModelScope.launch {
            getJewelleryCategoryUseCase(
                localDatabase.getToken().orEmpty(),
                isFrequentlyUse,
                firstCatId,
                secondCatId,
                thirdCatId
            ).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getJewelleryCategory.value = _getJewelleryCategory.value.copy(
                            loading = true
                        )
                    }
                    is Resource.Success -> {
                        _getJewelleryCategory.update { uiState ->
                            uiState.copy(
                                loading = false,
                                successLoading = result.data!!.map { it.asUiModel() }
                            )
                        }


                    }
                    is Resource.Error -> {
                        _getJewelleryCategory.value = _getJewelleryCategory.value.copy(
                            loading = false,
                        )
                        result.message?.let { errorString ->
                            _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                        }
                    }
                }
            }
        }
    }

    fun selectImage(id: String) {
        val groupImageList = _getJewelleryCategory.value.successLoading.orEmpty()
        groupImageList.filter {
            it.id != id
        }.forEach {
            it.isChecked = false
        }

        _getJewelleryCategory.update { uiState ->
            groupImageList.find {
                it.id == id
            }?.isChecked = groupImageList.find {
                it.id == id
            }?.isChecked!!.not()

//            groupImageList.find {
//                it.id == id
//            }?.let {
//                selectedJewelleryCategory.value = if (it.isChecked){
//                    it
//                }else{
//                    null
//                }
//            }

            uiState.copy(
                successLoading = groupImageList
            )

        }

    }


}