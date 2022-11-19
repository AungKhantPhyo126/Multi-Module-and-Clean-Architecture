package com.critx.data.network.dto.setupStock.jewelleryCategory.error

data class CreateCategoryError(
    val data: List<Any>,
    val response: Response
)

data class SimpleError(
    val response: ResponseSimple,
    val data: List<Any>
)

data class ResponseSimple(
    val message: String?
)

fun ResponseSimple.getMessage():List<String?>{
   return listOf<String?>(message)
}

//hashMap Error
data class ErrorWithHashMap(
    val response: ResponseWithHashMap,
    val data: List<Any>
)

data class ResponseWithHashMap(
    val status:String,

)