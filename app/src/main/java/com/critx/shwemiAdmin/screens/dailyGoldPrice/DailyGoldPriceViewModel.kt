package com.critx.shwemiAdmin.screens.dailyGoldPrice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.critx.commonkotlin.util.Resource
import com.critx.domain.useCase.auth.LogoutUseCase
import com.critx.domain.useCase.auth.RefreshTokenUseCase
import com.critx.domain.useCase.dailygoldprice.GetProfileUsecase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.dailygoldandprice.asUiModel
import com.critx.shwemiAdmin.uistate.LoginUiState
import com.critx.shwemiAdmin.uistate.LogoutUiState
import com.critx.shwemiAdmin.uistate.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyGoldPriceViewModel @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val logoutUseCase: LogoutUseCase,
    private val getProfileUsecase: GetProfileUsecase,
    private val refreshTokenUseCase: RefreshTokenUseCase
): ViewModel() {
    private val _logoutState = MutableStateFlow(LogoutUiState())
    val logoutState = _logoutState.asStateFlow()

    private val _profileState = MutableStateFlow(ProfileUiState())
    val profileState = _profileState.asStateFlow()

    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun isLogin():Boolean{
        return localDatabase.isLogin()
//        return true
    }

    fun isRefreshTokenExpire():Boolean{
        return localDatabase.isRefreshTokenExpire()
    }

    fun logout(){
        viewModelScope.launch {
            logoutUseCase(localDatabase.getToken().orEmpty()).collect {  result->
                when(result){
                    is Resource.Loading->{
                        _logoutState.value =_logoutState.value.copy(
                            loading = true
                        )
                    }
                    is Resource.Success->{
                        _logoutState.value =_logoutState.value.copy(
                            loading = false,
                            successMessage = "Login Success"
                        )
                        localDatabase.clearuser()
                    }
                    is Resource.Error->{
                        _logoutState.value =_logoutState.value.copy(
                            loading = false,
                        )
                        result.message?.let {
                            _event.emit(UiEvent.ShowErrorSnackBar(it))
                        }
                    }
                }

            }
        }
    }

    fun getProfile(){
        viewModelScope.launch {
            getProfileUsecase(localDatabase.getToken().orEmpty()).collect { result->
                when(result){
                    is Resource.Loading->{
                        _profileState.value =_profileState.value.copy(
                            loading = true
                        )
                    }
                    is Resource.Success->{
                        _profileState.value =_profileState.value.copy(
                            loading = false,
                            successLoading = result.data!!.asUiModel()
                        )
                    }
                    is Resource.Error->{
                        _profileState.value =_profileState.value.copy(
                            loading = false,
                        )
                        result.message?.let {errorString->
                            if (errorString == "You are not Authorized"){
                                refreshTokenUseCase(localDatabase.getToken().orEmpty()).collect { result
                                    when(result){
                                        is Resource.Loading->{}
                                        is Resource.Success->{
                                            localDatabase.deleteToken()
                                            localDatabase.saveToken(it.data?.token.orEmpty())
                                        }
                                        is Resource.Error->{
                                            _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                                        }
                                    }
                                }
                            }
                            _event.emit(UiEvent.ShowErrorSnackBar(errorString))
                        }
                    }
                }
            }
        }
    }
}