package com.critx.shwemiAdmin.screens.setupStock.first

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.useCase.SetUpStock.GetJewelleryTypeUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.dailygoldandprice.asUiModel
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
    private val _jewelleryTypeState = MutableStateFlow(JewelleryTypeUiState())
    val jewelleryTypeState = _jewelleryTypeState.asStateFlow()


    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun getJewelleryType(){
        viewModelScope.launch {
            getJewelleryTypeUseCase(localDatabase.getToken().orEmpty()).collectLatest { result->
                when(result){
                    is Resource.Loading->{
                        _jewelleryTypeState.value =_jewelleryTypeState.value.copy(
                            loading = true
                        )
                    }
                    is Resource.Success->{
                        _jewelleryTypeState.value =_jewelleryTypeState.value.copy(
                            loading = false,
                            successLoading = result.data!!.map { it.asUiModel() }
                        )
                    }
                    is Resource.Error->{
                        _jewelleryTypeState.value =_jewelleryTypeState.value.copy(
                            loading = false,
                        )
                        result.message?.let {errorString->
                            _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                        }
                    }
                }
            }
        }
    }
}