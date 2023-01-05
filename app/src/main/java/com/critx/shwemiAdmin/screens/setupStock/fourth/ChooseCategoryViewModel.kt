package com.critx.shwemiAdmin.screens.setupStock.third

import androidx.lifecycle.*
import com.critx.commonkotlin.util.Resource
import com.critx.domain.useCase.SetUpStock.DeleteJewelleryCategoryUseCase
import com.critx.domain.useCase.SetUpStock.GetJewelleryCategoryUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryCategoryUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
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

    private val _getJewelleryCategoryLiveData = MutableLiveData<Resource<List<JewelleryCategoryUiModel>>>()
    val getJewelleryCategoryLiveData :LiveData<Resource<List<JewelleryCategoryUiModel>>>
get() = _getJewelleryCategoryLiveData
    private val _deleteLiveData = MutableLiveData<Resource<String>>()
    val deleteLiveData :LiveData<Resource<String>>
        get() = _deleteLiveData

    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun deleteJewelleryCategory(id: String) {
        val method = "DELETE"
        val methodRequestBody = method.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        viewModelScope.launch {
            deleteJewelleryCategoryUseCase(
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
                        _getJewelleryCategoryLiveData.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _getJewelleryCategoryLiveData.value =Resource.Success(result.data!!.map { it.asUiModel() })


                    }
                    is Resource.Error -> {
                        _getJewelleryCategoryLiveData.value = Resource.Error(result.message)
                    }
                }
            }
        }
    }


    fun selectImage(id: String) {

        _getJewelleryCategoryLiveData.value!!.data!!.filterNotNull().filter {
            it.id != id
        }.forEach {
            it.isChecked = false
        }

        _getJewelleryCategoryLiveData.value!!.data!!.filterNotNull().find {
            it.id == id
        }?.isChecked = _getJewelleryCategoryLiveData.value!!.data!!.filterNotNull().find {
            it.id == id
        }?.isChecked!!.not()

        _getJewelleryCategoryLiveData.value = _getJewelleryCategoryLiveData.value

    }
    fun deSelectAll() {
        _getJewelleryCategoryLiveData.value!!.data!!.filterNotNull().forEach {
            it.isChecked = false
        }
        _getJewelleryCategoryLiveData.value = _getJewelleryCategoryLiveData.value
    }


}