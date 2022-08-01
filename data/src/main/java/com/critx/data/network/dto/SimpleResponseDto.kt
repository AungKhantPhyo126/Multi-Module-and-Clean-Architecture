package com.critx.data.network.dto

import com.critx.domain.model.SimpleData

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