package com.critx.shwemiAdmin.screens.setupStock.first

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.domain.useCase.SetUpStock.GetJewelleryTypeUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.uiModel.dailygoldandprice.asUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryTypeUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
import com.critx.shwemiAdmin.uistate.JewelleryTypeUiState
import com.critx.shwemiAdmin.uistate.LogoutUiState
import com.critx.shwemiAdmin.uistate.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupStockViewModel @Inject constructor(
    private val getJewelleryTypeUseCase: GetJewelleryTypeUseCase,
    private val localDatabase: LocalDatabase
):ViewModel() {
    private val _jewelleryTypeLiveData = MutableLiveData<Resource<List<JewelleryTypeUiModel>>>()
    val jewelleryTypeState :LiveData<Resource<List<JewelleryTypeUiModel>>>
get() = _jewelleryTypeLiveData


    init {
        getJewelleryType()
    }

    fun getJewelleryType(){
        viewModelScope.launch {
            getJewelleryTypeUseCase(localDatabase.getToken().orEmpty()).collectLatest { result->
                when(result){
                    is Resource.Loading->{
                        _jewelleryTypeLiveData.value =Resource.Loading()
                    }
                    is Resource.Success->{
                        _jewelleryTypeLiveData.value =Resource.Success(result.data!!.map { it.asUiModel() })
                    }
                    is Resource.Error->{
                        _jewelleryTypeLiveData.value =Resource.Error(result.message)
                    }
                }
            }
        }
    }
}