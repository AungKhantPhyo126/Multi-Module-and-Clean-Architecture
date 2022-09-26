package com.critx.shwemiAdmin

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<MutableList<T>>.notifyObserver() {
    this.value=this.value
}