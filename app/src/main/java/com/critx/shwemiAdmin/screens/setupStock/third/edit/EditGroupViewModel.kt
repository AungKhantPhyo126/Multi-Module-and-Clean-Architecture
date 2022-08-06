package com.critx.shwemiAdmin.screens.setupStock.third.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.useCase.SetUpStock.CreateJewelleryGroupUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
import com.critx.shwemiAdmin.uistate.JewelleryGroupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditGroupViewModel @Inject constructor(
    private val createJewelleryGroupUseCase: CreateJewelleryGroupUseCase,
    private val localDatabase: LocalDatabase
):ViewModel() {
    var selectedImgUri: File? = null


    private val _createJewelleryGroupState = MutableStateFlow(JewelleryGroupUiState())
    val createJewelleryGroupState = _createJewelleryGroupState.asStateFlow()


    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun createGroup(
        image: MultipartBody.Part,
        jewellery_type_id : RequestBody,
        jewellery_quality_id : RequestBody,
        is_frequently_used : RequestBody,
        name : RequestBody
    ){
        viewModelScope.launch {
            createJewelleryGroupUseCase(
                localDatabase.getToken().orEmpty(),
                image, jewellery_type_id, jewellery_quality_id, is_frequently_used, name
            ).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _createJewelleryGroupState.value =_createJewelleryGroupState.value.copy(
                            createGroupLoading = true
                        )
                    }
                    is Resource.Success->{
                        _createJewelleryGroupState.value =_createJewelleryGroupState.value.copy(
                            createGroupLoading = false,
                            createSuccessLoading = "Group Created"
                        )
                    }
                    is Resource.Error->{
                        _createJewelleryGroupState.value =_createJewelleryGroupState.value.copy(
                            createGroupLoading = false,
                        )
                        it.message?.let {errorString->
                            _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                        }
                    }
                }
            }
        }
    }
}