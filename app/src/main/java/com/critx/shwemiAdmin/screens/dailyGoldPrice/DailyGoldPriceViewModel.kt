package com.critx.shwemiAdmin.screens.dailyGoldPrice

import androidx.lifecycle.ViewModel
import com.critx.common.LocalDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DailyGoldPriceViewModel @Inject constructor() : ViewModel() {
    fun isLogin():Boolean{
//        return localDatabase.isLogin()
        return true
    }
}