package com.critx.shwemiAdmin.screens.setupStock.third

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.critx.commonkotlin.util.Resource
import com.critx.domain.useCase.SetUpStock.CreateJewelleryGroupUseCase
import com.critx.domain.useCase.SetUpStock.GetJewelleryGroupUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
import com.critx.shwemiAdmin.uistate.JewelleryGroupUiState
import com.critx.shwemiAdmin.uistate.JewelleryQualityUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseGroupViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getJewelleryGroupUseCase: GetJewelleryGroupUseCase
) : ViewModel() {

    private val _getGroupState = MutableStateFlow(JewelleryGroupUiState())
    val getGroupState = _getGroupState.asStateFlow()

    //    fun setImageList(list:MutableList<ChooseGroupUIModel>){
//        groupImageList.addAll(list)
//        _groupImages.value=groupImageList
//    }
//
    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()


//    var jewelleryGroupLive:LiveData<PagingData<ChooseGroupUIModel>> =jewelleryGroupPagingRepo.getJewelleryGroupPaging().map { pagingData ->
//            pagingData.map {
//                it.asUiModel()
//            }
//        }.cachedIn(viewModelScope).asLiveData()


    fun getJewelleryGroup(isFrequentlyUse: Int,firstCatId:Int,secondCatId:Int) {
        viewModelScope.launch {
            getJewelleryGroupUseCase(
                localDatabase.getToken().orEmpty(),
                isFrequentlyUse,
                firstCatId,
                secondCatId
            ).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getGroupState.value = _getGroupState.value.copy(
                            loading = true
                        )
                    }
                    is Resource.Success -> {
                     _getGroupState.update { uiState ->
                            uiState.copy(
                             loading = false,
                             successLoading = result.data?.data!!.map { it.asUiModel() }
                         )
                        }


                    }
                    is Resource.Error -> {
                        _getGroupState.value = _getGroupState.value.copy(
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
//        groupImageList.find {
//            it.id == id
//        }?.isChecked = groupImageList.find {
//            it.id == id
//        }?.isChecked!!.not()
//
//        groupImageList.filter {
//            it.id != id
//        }.forEach {
//            it.isChecked = false
//        }
//
//        _groupImages.value = groupImageList
    }


}