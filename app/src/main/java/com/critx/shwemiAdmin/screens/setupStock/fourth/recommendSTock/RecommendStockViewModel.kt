package com.critx.shwemiAdmin.screens.setupStock.fourth.recommendSTock

import androidx.lifecycle.ViewModel
import com.critx.domain.useCase.SetUpStock.GetJewelleryCategoryUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uistate.JewelleryCategoryUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class RecommendStockViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val getJewelleryCategoryUseCase: GetJewelleryCategoryUseCase
) : ViewModel() {
    private val _getRecommendStock = MutableStateFlow(JewelleryCategoryUiState())
    val getRecommendStock = _getRecommendStock.asStateFlow()

    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()


}