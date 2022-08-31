package com.critx.shwemiAdmin.screens.setupStock.third.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.useCase.SetUpStock.CreateJewelleryGroupUseCase
import com.critx.domain.useCase.SetUpStock.EditJewelleryGroupUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.SelectedImage
import com.critx.shwemiAdmin.uiModel.setupStock.asUiModel
import com.critx.shwemiAdmin.uistate.JewelleryGroupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditGroupViewModel @Inject constructor(
    private val createJewelleryGroupUseCase: CreateJewelleryGroupUseCase,
    private val editJewelleryGroupUseCase: EditJewelleryGroupUseCase,
    private val localDatabase: LocalDatabase
):ViewModel() {
    var selectedImgUri= MutableLiveData<SelectedImage?>(null)
    fun setSelectedImgUri(selectedImage: SelectedImage?){
        selectedImgUri.value = selectedImage
    }


    private val _createJewelleryGroupState = MutableStateFlow(JewelleryGroupUiState())
    val createJewelleryGroupState = _createJewelleryGroupState.asStateFlow()

    private val _editJewelleryGroupState = MutableStateFlow(JewelleryGroupUiState())
    val editJewelleryGroupState = _editJewelleryGroupState.asStateFlow()


    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun editGroup(
        image: MultipartBody.Part,
        groupId:String,
        jewellery_type_id : RequestBody,
        jewellery_quality_id : RequestBody,
        is_frequently_used : RequestBody,
        name : RequestBody
    ){
        val method = "PUT"
        val methodRequestBody =method.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        viewModelScope.launch {
            editJewelleryGroupUseCase(
                localDatabase.getToken().orEmpty(),methodRequestBody,groupId,
                image, jewellery_type_id, jewellery_quality_id, is_frequently_used, name
            ).collectLatest {
                when(it){
                    is Resource.Loading->{
                        _editJewelleryGroupState.value =_editJewelleryGroupState.value.copy(
                            editGroupLoading = true
                        )
                    }
                    is Resource.Success->{

                        _editJewelleryGroupState.value =_editJewelleryGroupState.value.copy(
                            editGroupLoading = false,
                            editSuccessLoading = "Group Created"
                        )
                    }
                    is Resource.Error->{
                        _editJewelleryGroupState.value =_editJewelleryGroupState.value.copy(
                            editGroupLoading = false,
                        )
                        it.message?.let {errorString->
                            _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                        }
                    }
                }
            }
        }
    }

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
                            createSuccessLoading = it.data!!.asUiModel()
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