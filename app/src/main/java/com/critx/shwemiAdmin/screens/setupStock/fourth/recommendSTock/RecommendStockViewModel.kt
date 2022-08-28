package com.critx.shwemiAdmin.screens.setupStock.fourth.recommendSTock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.useCase.SetUpStock.GetJewelleryCategoryUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryCategoryUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
import com.critx.shwemiAdmin.uistate.JewelleryCategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendStockViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getJewelleryCategoryUseCase: GetJewelleryCategoryUseCase
) : ViewModel() {


    private val _getRecommendCategory = MutableStateFlow(JewelleryCategoryUiState())
    val getRecommendCategory = _getRecommendCategory.asStateFlow()

    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun getJewelleryCategory(isFrequentlyUse: Int?,firstCatId:Int?,secondCatId:Int?,thirdCatId:Int?) {
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
                        _getRecommendCategory.value = _getRecommendCategory.value.copy(
                            loading = true
                        )
                    }
                    is Resource.Success -> {
                        _getRecommendCategory.update { uiState ->

                            uiState.copy(
                                loading = false,
                                successLoading = result.data!!.map { it.asUiModel() }
                            )
                        }


                    }
                    is Resource.Error -> {
                        _getRecommendCategory.value = _getRecommendCategory.value.copy(
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
        val groupImageList = _getRecommendCategory.value.successLoading.orEmpty()

        _getRecommendCategory.update { uiState ->
            groupImageList.find {
                it.id == id
            }?.isChecked = groupImageList.find {
                it.id == id
            }?.isChecked!!.not()

            uiState.copy(
                successLoading = groupImageList
            )

        }

    }


}