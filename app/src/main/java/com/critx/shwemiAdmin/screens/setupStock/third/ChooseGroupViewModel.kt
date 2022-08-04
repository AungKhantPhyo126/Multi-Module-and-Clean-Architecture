package com.critx.shwemiAdmin.screens.setupStock.third

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.pagingDataSource.pagingRepo.JewelleryGroupPagingRepo
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
    private val jewelleryGroupPagingRepo: JewelleryGroupPagingRepo
) : ViewModel() {
//    private var _groupImages= MutableLiveData<MutableList<ChooseGroupUIModel>>()
//    val groupImages : LiveData<MutableList<ChooseGroupUIModel>>
//        get() = _groupImages
//    private val groupImageList = mutableListOf<ChooseGroupUIModel>()
//
//    fun setImageList(list:MutableList<ChooseGroupUIModel>){
//        groupImageList.addAll(list)
//        _groupImages.value=groupImageList
//    }
//

    private val _jewelleryGroupState = MutableStateFlow(JewelleryGroupUiState())
    val jewelleryGroupState = _jewelleryGroupState.asStateFlow()


    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()


    var jewelleryGroupLive:LiveData<PagingData<ChooseGroupUIModel>> =jewelleryGroupPagingRepo.getJewelleryGroupPaging().map { pagingData ->
            pagingData.map {
                it.asUiModel()
            }
        }.cachedIn(viewModelScope).asLiveData()



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