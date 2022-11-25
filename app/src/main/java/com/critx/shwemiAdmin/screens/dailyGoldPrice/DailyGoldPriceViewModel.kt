package com.critx.shwemiAdmin.screens.dailyGoldPrice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.dailyGoldAndPrice.RebuyPriceSmallAndLargeDomain
import com.critx.domain.useCase.auth.LogoutUseCase
import com.critx.domain.useCase.auth.RefreshTokenUseCase
import com.critx.domain.useCase.dailygoldprice.*
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.localDatabase.LocalDatabase
import com.critx.shwemiAdmin.uiModel.dailygoldandprice.GoldPriceUIModel
import com.critx.shwemiAdmin.uiModel.dailygoldandprice.asUiModel
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
    private val getGoldPriceUseCase: GetGoldPriceUseCase,
    private val updateGoldPriceUseCase: UpdateGoldPriceUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val updateRebuyPriceUseCase: UpdateRebuyPriceUseCase,
    private val getRebuyPriceUseCase: GetRebuyPriceUseCase
) : ViewModel() {
    private val _logoutState = MutableStateFlow(LogoutUiState())
    val logoutState = _logoutState.asStateFlow()

    private val _profileState = MutableStateFlow(ProfileUiState())
    val profileState = _profileState.asStateFlow()

    private var _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean>
        get() = _isLogin

    private val _getRebuyPriceLive = MutableLiveData<Resource<RebuyPriceSmallAndLargeDomain>>()
    val getRebuyPriceLive: LiveData<Resource<RebuyPriceSmallAndLargeDomain>>
        get() = _getRebuyPriceLive

    private val _getGoldPriceLive = MutableLiveData<Resource<List<GoldPriceUIModel>>>()
    val getGoldPriceLive: LiveData<Resource<List<GoldPriceUIModel>>>
        get() = _getGoldPriceLive

    private val _updateGoldLive = MutableLiveData<Resource<String>>()
    val updateGoldLive: LiveData<Resource<String>>
        get() = _updateGoldLive

    private val _updateRebuyPrice = MutableLiveData<Resource<String>>()
    val updateRebuyPrice: LiveData<Resource<String>>
        get() = _updateRebuyPrice

    fun resetUpdateGoldLive() {
        _updateGoldLive.value = null
    }

    fun isloggedIn() {
        _isLogin.value = localDatabase.isLogin()
//        return true
    }

    init {
        isloggedIn()
    }

    fun isRefreshTokenExpire(): Boolean {
        return localDatabase.isRefreshTokenExpire()
    }



    fun updateRebuyPrice(
        horizontal_option_name: HashMap<String, String>,
        vertical_option_name: HashMap<String, String>,
        horizontal_option_level: HashMap<String, String>,
        vertical_option_level: HashMap<String, String>,
        size: HashMap<String, String>,
        price: HashMap<String, String>
    ) {
        viewModelScope.launch {
            updateRebuyPriceUseCase(
                localDatabase.getToken().orEmpty(),
                horizontal_option_name,
                vertical_option_name,
                horizontal_option_level,
                vertical_option_level,
                size,
                price
            ).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _updateRebuyPrice.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _updateRebuyPrice.value = Resource.Success(it.data!!.message)

                    }
                    is Resource.Error -> {
                        _updateRebuyPrice.value = Resource.Error(it.message)

                    }
                }
            }
        }
    }

    fun updateGoldPrice(price: HashMap<String, String>) {
        viewModelScope.launch {
            updateGoldPriceUseCase(localDatabase.getToken().orEmpty(), price).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _updateGoldLive.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _updateGoldLive.value = Resource.Success(it.data!!.message)

                    }
                    is Resource.Error -> {
                        _updateGoldLive.value = Resource.Error(it.message)

                    }
                }
            }
        }
    }

    fun getGoldPrice() {
        viewModelScope.launch {
            getGoldPriceUseCase(localDatabase.getToken().orEmpty()).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _getGoldPriceLive.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _getGoldPriceLive.value = Resource.Success(it.data!!.map { it.asUiModel() })

                    }
                    is Resource.Error -> {
                        _getGoldPriceLive.value = Resource.Error(it.message)

                    }
                }
            }
        }
    }

    fun getRebuyPrice() {
        viewModelScope.launch {
            getRebuyPriceUseCase(localDatabase.getToken().orEmpty()).collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        _getRebuyPriceLive.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        _getRebuyPriceLive.value = Resource.Success(it.data!!)

                    }
                    is Resource.Error -> {
                        _getRebuyPriceLive.value = Resource.Error(it.message)

                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase(localDatabase.getToken().orEmpty()).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _logoutState.value = _logoutState.value.copy(
                            loading = true
                        )
                    }
                    is Resource.Success -> {
                        _logoutState.value = _logoutState.value.copy(
                            loading = false,
                            successMessage = "Login Success"
                        )
                        localDatabase.clearuser()
                        isloggedIn()
                    }
                    is Resource.Error -> {
                        _logoutState.value = _logoutState.value.copy(
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

    fun getProfile() {
        var count = 0
        viewModelScope.launch {
            getProfileUsecase(localDatabase.getToken().orEmpty()).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _profileState.value = _profileState.value.copy(
                            loading = true
                        )
                    }
                    is Resource.Success -> {
                        getGoldPrice()
                        _profileState.value = _profileState.value.copy(
                            loading = false,
                            successLoading = result.data!!.asUiModel()
                        )
                    }
                    is Resource.Error -> {
                        if (count == 0) {
                            getProfile()
                            count++
                        }
                        _profileState.value = _profileState.value.copy(
                            loading = false,
                        )
//                        result.message?.let {errorString->
//                            if (errorString == "You are not Authorized" || errorString == "Bad request"){
//                                refreshTokenUseCase(localDatabase.getToken().orEmpty()).collect { result
//                                    when(result){
//                                        is Resource.Loading->{
//
//                                        }
//                                        is Resource.Success->{
//                                            localDatabase.deleteToken()
//                                            localDatabase.saveToken(it.data?.token.orEmpty())
//                                            getProfile()
//                                        }
//                                        is Resource.Error->{
//                                            localDatabase.clearuser()
//                                            isloggedIn()
//                                            _event.emit(UiEvent.ShowErrorSnackBar("refresh token fail"))
//                                        }
//                                    }
//                                }
//                            }
//                            _event.emit(UiEvent.ShowErrorSnackBar(errorString))
//                        }
                    }
                }
            }
        }
    }
}