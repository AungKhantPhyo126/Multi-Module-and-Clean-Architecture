package com.critx.data

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.ResponseBody
import org.json.JSONObject
import java.util.ArrayList

inline fun <reified T> ResponseBody.parseErrorWithDataClass(errorJsonString:String): T? {
    val moshi = Moshi.Builder().build()
    val builder = Moshi.Builder().build()
//    val parser = moshi.adapter(T::class.java)
    val parser = moshi.adapter(T::class.java)

    try {
        return parser.fromJson(errorJsonString)
    } catch (e: JsonDataException) {
        e.printStackTrace()
    }
    return null
}


fun getErrorMessageFromHashMap(errorMessage:Map<String,List<String>>):String{
    val list: List<Map.Entry<String, Any>> =
        ArrayList<Map.Entry<String, Any>>(errorMessage.entries)
    val (key, value) = list[0]
    return value.toString()
}

inline fun ResponseBody.parseError(errorJsonString: String): Map<String, List<String>>? {
    val moshi = Moshi.Builder().build()
    val type =
        Types.newParameterizedType(Map::class.java, String::class.java, List::class.javaObjectType)
    val adapter = moshi.adapter<Map<String, List<String>>>(type)
    var jsonObject = JSONObject(errorJsonString)


    try {
        return adapter.fromJson(
            jsonObject.getJSONObject("response").getJSONObject("message").toString()
        )
    } catch (e: JsonDataException) {
        e.printStackTrace()
    }
    return null
}

inline fun ResponseBody.parseErrorSingle(): String?{
    val moshi = Moshi.Builder().build()
    val type = Types.newParameterizedType(Map::class.java, String::class.java, List::class.javaObjectType)
    val singleMessageType = Types.newParameterizedType(Map::class.java, String::class.java, String::class.java)

    val adapter = moshi.adapter<Map<String, List<String>>>(type)
    val singleMessageAdapter = moshi.adapter<Map<String, String>>(type)
    val jsonObject = JSONObject(this.string())
    val singleMessage =  jsonObject.getJSONObject("response").getJSONObject("message").toString()

    var singleMessageResult:String? = null

    val maxTries = 1
    var count = 0
    try {
        val messageList = adapter.fromJson(jsonObject.getJSONObject("response").getJSONObject("message").toString())
        val list: List<Map.Entry<String, Any>> =
            ArrayList<Map.Entry<String, Any>>(messageList!!.entries)
        val (key, value) = list[0]
        return value.toString()
    }catch (e: JsonDataException) {
        val singleMessageMap = singleMessageAdapter.fromJson(singleMessage)
        val list: List<Map.Entry<String, Any>> =
            ArrayList<Map.Entry<String, Any>>(singleMessageMap!!.entries)
        val (key, value) = list[0]
        singleMessageResult = value.toString()
        if (++count == maxTries) throw e
    }
    return singleMessageResult

}


