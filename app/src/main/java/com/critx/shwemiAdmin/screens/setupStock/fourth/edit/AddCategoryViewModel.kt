package com.critx.shwemiAdmin.screens.setupStock.fourth.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.useCase.SetUpStock.CreateJewelleryGroupUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uistate.JewelleryGroupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val createJewelleryGroupUseCase: CreateJewelleryGroupUseCase,
    private val localDatabase: LocalDatabase
):ViewModel() {
    var selectedImgUri: File? = null
    var selectedVideoUri: File? = null



}