package com.critx.shwemiAdmin.screens.setupStock.second

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.domain.useCase.SetUpStock.GetJewelleryQualityUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryQualityUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
import com.critx.shwemiAdmin.uistate.JewelleryQualityUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JewelleryQualityViewModel @Inject constructor(
    private val getJewelleryQualityUseCase: GetJewelleryQualityUseCase,
    private val localDatabase: LocalDatabase
): ViewModel() {
    private val _jewlleryQualityLiveData =MutableLiveData<Resource<List<JewelleryQualityUiModel>>>()
    val jewelleryQualityLiveData :LiveData<Resource<List<JewelleryQualityUiModel>>>
    get() = _jewlleryQualityLiveData

    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()



    fun getJewelleryQuality(){
        viewModelScope.launch {
            getJewelleryQualityUseCase(localDatabase.getToken().orEmpty()).collectLatest { result->
                when(result){
                    is Resource.Loading->{
                        _jewlleryQualityLiveData.value = Resource.Loading()
                    }
                    is Resource.Success->{
                        _jewlleryQualityLiveData.value =Resource.Success(result.data!!.map { it.asUiModel() })
                    }
                    is Resource.Error->{
                        _jewlleryQualityLiveData.value =Resource.Error(result.message)
                    }
                }
            }
        }
    }
}