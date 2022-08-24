package com.critx.shwemiAdmin

sealed class UiEvent {
    data class ShowErrorSnackBar(val message: String): UiEvent()
//    data class ShowNoInternet(val message: String):UiEvent()
}