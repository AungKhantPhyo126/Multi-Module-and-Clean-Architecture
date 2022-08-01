package com.critx.shwemiAdmin.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.common.LocalDatabase
import com.critx.commonkotlin.util.Resource
import com.critx.domain.useCase.auth.LogInUseCase
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.uistate.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val logInUseCase: LogInUseCase,
    private val localDatabase: LocalDatabase
):ViewModel(){
    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun login(name:String,password:String){
        viewModelScope.launch {
            logInUseCase(name,password).collect { result->
                when(result){
                    is Resource.Loading->{
                        _state.value =_state.value.copy(
                            loading = true
                        )
                    }
                    is Resource.Success->{
                        _state.value =_state.value.copy(
                            loading = false,
                            successMessage = "Login Success"
                        )
                        localDatabase.saveToken(result.data!!.token)
                    }
                    is Resource.Error->{
                        _state.value =_state.value.copy(
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
}