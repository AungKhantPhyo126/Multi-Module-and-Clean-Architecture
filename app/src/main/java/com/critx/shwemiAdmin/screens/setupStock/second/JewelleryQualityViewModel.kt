package com.critx.shwemiAdmin.screens.setupStock.second

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.useCase.SetUpStock.GetJewelleryQualityUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
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
    private val _jewelleryQualityState = MutableStateFlow(JewelleryQualityUiState())
    val jewelleryQualityState = _jewelleryQualityState.asStateFlow()


    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()



    fun getJewelleryQuality(){
        viewModelScope.launch {
            getJewelleryQualityUseCase(localDatabase.getToken().orEmpty()).collectLatest { result->
                when(result){
                    is Resource.Loading->{
                        _jewelleryQualityState.value =_jewelleryQualityState.value.copy(
                            loading = true
                        )
                    }
                    is Resource.Success->{
                        _jewelleryQualityState.value =_jewelleryQualityState.value.copy(
                            loading = false,
                            successLoading = result.data!!.map { it.asUiModel() }
                        )
                    }
                    is Resource.Error->{
                        _jewelleryQualityState.value =_jewelleryQualityState.value.copy(
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