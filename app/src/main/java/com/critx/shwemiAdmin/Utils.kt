package com.critx.shwemiAdmin

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.FragmentActivity
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
fun hideKeyboard(activity: FragmentActivity?, view: View) {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}