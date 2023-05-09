package com.critx.shwemiAdmin.screens.confirmVoucher.unconfirmVoucher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.domain.model.voucher.UnConfirmVoucherDomain
import com.critx.domain.useCase.voucher.GetUnConfirmVouchersUseCase
import com.critx.shwemiAdmin.uiModel.checkUpTransfer.BoxScanUIModel
import com.critx.shwemiAdmin.uiModel.checkUpTransfer.asUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnConfirmVoucherViewModel @Inject constructor(
  private val localDatabase: LocalDatabase,
  private val getUnConfirmVouchersUseCase: GetUnConfirmVouchersUseCase
) :ViewModel() {
  private var _getUnConfirmVoucherLive = MutableLiveData<Resource<List<UnConfirmVoucherDomain>>>()
  val getUnConfirmVoucherLive: LiveData<Resource<List<UnConfirmVoucherDomain>>>
    get() = _getUnConfirmVoucherLive

  fun getVouchers(type:String){
    viewModelScope.launch {
      getUnConfirmVouchersUseCase(localDatabase.getToken().orEmpty(),type).collectLatest {
        when (it) {
          is Resource.Loading -> {
            _getUnConfirmVoucherLive.value = Resource.Loading()
          }
          is Resource.Success -> {
            _getUnConfirmVoucherLive.value = Resource.Success(it.data!!)

          }
          is Resource.Error -> {
            _getUnConfirmVoucherLive.value = Resource.Error(it.message)

          }
        }
      }
    }
  }

}