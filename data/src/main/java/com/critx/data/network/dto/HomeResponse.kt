package com.critx.data.network.dto

import com.critx.domain.model.Home

data class HomeResponse(
    val id:String,
    val name:String
)

fun HomeResponse.asDomain():Home{
    return Home(
        id = id,
        name = name
    )
}
