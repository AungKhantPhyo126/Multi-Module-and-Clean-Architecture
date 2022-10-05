package com.critx.shwemiAdmin

import androidx.lifecycle.MutableLiveData
import com.critx.commonkotlin.util.Resource

fun <T> MutableLiveData<MutableList<T>>.notifyObserver() {
    this.value=this.value
}

fun <T> MutableLiveData<Resource<MutableList<T>>>.notifyObserverWithResource() {
    this.value=this.value
}