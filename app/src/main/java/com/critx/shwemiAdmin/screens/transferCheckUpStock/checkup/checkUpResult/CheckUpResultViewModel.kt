package com.critx.shwemiAdmin.screens.transferCheckUpStock.checkup.checkUpResult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.critx.domain.useCase.box.GetBoxDataUseCase
import com.critx.domain.useCase.collectStock.ScanProductCodeUseCase
import com.critx.domain.useCase.transferCheckUp.CheckUpUseCase
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckUpResultViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
): ViewModel() {

    private var _stockListLive = MutableLiveData<MutableList<StockCodeForListUiModel>>()
    val stockListLive: LiveData<MutableList<StockCodeForListUiModel>>
        get() = _stockListLive
}
