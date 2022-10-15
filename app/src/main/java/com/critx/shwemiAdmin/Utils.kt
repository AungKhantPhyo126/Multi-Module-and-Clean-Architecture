package com.critx.shwemiAdmin

import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.MutableLiveData
import com.critx.commonkotlin.util.Resource

fun <T> MutableLiveData<MutableList<T>>.notifyObserver() {
    this.value=this.value
}

fun <T> MutableLiveData<Resource<MutableList<T>>>.notifyObserverWithResource() {
    this.value!!.data=this.value!!.data
}

fun AutoCompleteTextView.showDropdown(adapter: ArrayAdapter<String>?) {
    if (!TextUtils.isEmpty(this.text.toString())) {
        adapter?.filter?.filter(null)
    }
}
