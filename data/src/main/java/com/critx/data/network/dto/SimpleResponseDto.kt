package com.critx.data.network.dto

import com.critx.domain.model.SimpleData

data class SimpleResponse(
    val response:SimpleResponseDto
)

data class SimpleResponseDto(
    val status:String,
    val message:String
)
fun SimpleResponseDto.asDomain():SimpleData{
    return SimpleData(
        status = status,
        message = message
    )
}

data class SimpleResponseWithData(
    val response:SimpleResponseDto,
    val data: DataWithId
)
data class DataWithId(
    val id:String
)
fun SimpleResponseWithData.asDomain():SimpleData{
    return SimpleData(
        status = response.status,
        message = response.message,
        id = data.id
    )
}
