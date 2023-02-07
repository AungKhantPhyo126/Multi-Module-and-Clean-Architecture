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
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.ResponseBody
import org.json.JSONObject

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
inline fun ResponseBody.parseError(): Map<String,List<String>>?{
    val moshi = Moshi.Builder().build()
    val type = Types.newParameterizedType(Map::class.java, String::class.java, List::class.javaObjectType)
    val adapter = moshi.adapter<Map<String, List<String>>>(type)
    var jsonObject = JSONObject(this.string())


    try {
        return adapter.fromJson(jsonObject.getJSONObject("response").getJSONObject("message").toString())
    }catch (e: JsonDataException) {
        e.printStackTrace()
    }
    return null
}

fun getKyatsFromKPY(kyat:Int,pae:Int,ywae:Double):Double{
    return kyat+pae/ 16 + ywae/128
}

fun getYwaeFromKPY(kyat:Int,pae:Int,ywae:Double):Double{
    return kyat * 128 + pae * 8 + ywae
}

fun getKPYFromKyat(kyat: Double): List<String> {
    val resultList = mutableListOf<String>()
    var kpy_kyat = kyat.toInt()
    var decimal_pae = (kyat - kpy_kyat) * 16
    var kpy_pae = decimal_pae.toInt()
    var kpy_ywae = (decimal_pae - kpy_pae) * 8;

    resultList.add(kpy_kyat.toString())
    resultList.add(kpy_pae.toString())
    resultList.add(String.format("%.2f", kpy_ywae))
    return resultList
}

fun getKPYFromYwae(ywae: Double):List<String>{
    val resultList = mutableListOf<String>()
    var pae = (ywae / 8).toInt()
    var resultYwae = ywae % 8

    var kyat = (pae / 16) as Int
    pae = pae % 16
    resultList.add(kyat.toInt().toString())
    resultList.add(pae.toInt().toString())
    resultList.add(String.format("%.2f", resultYwae))
    return resultList
}

fun getYwaeFromGram(gram:Double):String{
    val result =  gram / 16.6 * 128;
    return String.format("%.2f", result)
}



