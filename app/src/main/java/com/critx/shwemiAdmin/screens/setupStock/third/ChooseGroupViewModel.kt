package com.critx.shwemiAdmin.screens.setupStock.third

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.domain.useCase.SetUpStock.CreateJewelleryGroupUseCase
import com.critx.domain.useCase.SetUpStock.DeleteJewelleryGroupUseCase
import com.critx.domain.useCase.SetUpStock.GetJewelleryGroupUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
import com.critx.shwemiAdmin.uistate.JewelleryGroupUiState
import com.critx.shwemiAdmin.uistate.JewelleryQualityUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class ChooseGroupViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getJewelleryGroupUseCase: GetJewelleryGroupUseCase,
    private val deleteJewelleryGroupUseCase: DeleteJewelleryGroupUseCase
) : ViewModel() {
    //forselection
//
//    var selectedChooseGroupUIModel = MutableLiveData<ChooseGroupUIModel?>(null)
//    fun setSelectGroup(selecctedGroup: ChooseGroupUIModel?) {
//        selectedChooseGroupUIModel.value = selecctedGroup
//    }

    private val _getGroupLiveData = MutableLiveData<Resource<List<ChooseGroupUIModel?>>>()
    val getGroupLiveData: LiveData<Resource<List<ChooseGroupUIModel?>>>
        get() = _getGroupLiveData

    private val _deleteLiveData = MutableLiveData<Resource<String>>()
    val deleteLiveData :LiveData<Resource<String>>
    get() = _deleteLiveData

    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()


    fun getJewelleryGroup(isFrequentlyUse: Int, firstCatId: Int, secondCatId: Int) {
        viewModelScope.launch {
            getJewelleryGroupUseCase(
                localDatabase.getToken().orEmpty(),
                isFrequentlyUse,
                firstCatId,
                secondCatId
            ).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _getGroupLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        val resultdata = mutableListOf<ChooseGroupUIModel?>(null)
                        resultdata.addAll(result.data!!.data.map { it.asUiModel() })
                        _getGroupLiveData.value =
                            Resource.Success(resultdata)

                    }
                    is Resource.Error -> {
                        _getGroupLiveData.value = Resource.Error(result.message)
                    }
                }
            }
        }
    }

    fun deleteJewelleryGroup(id: String) {
        val method = "DELETE"
        val methodRequestBody = method.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        viewModelScope.launch {
            deleteJewelleryGroupUseCase(
                localDatabase.getToken().orEmpty(), methodRequestBody, id
            ).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _deleteLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _deleteLiveData.value = Resource.Success(result.data!!.message)
                    }
                    is Resource.Error -> {
                       _deleteLiveData.value = Resource.Error(result.message)
                    }
                }
            }
        }
    }

    fun selectImage(id: String) {
        viewModelScope.launch {
            _getGroupLiveData.value!!.data!!.filterNotNull().filter {
                it.id != id
            }.forEach {
                it.isChecked = false
            }

            _getGroupLiveData.value!!.data!!.filterNotNull().find {
                it.id == id
            }?.isChecked = _getGroupLiveData.value!!.data!!.filterNotNull().find {
                it.id == id
            }?.isChecked!!.not()

            _getGroupLiveData.value = _getGroupLiveData.value
        }
    }
    fun deSelectAll() {
        _getGroupLiveData.value!!.data!!.filterNotNull().forEach {
            it.isChecked = false
        }
        _getGroupLiveData.value = _getGroupLiveData.value
    }


}