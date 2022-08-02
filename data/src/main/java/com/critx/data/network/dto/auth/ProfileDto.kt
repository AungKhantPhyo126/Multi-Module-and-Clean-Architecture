package com.critx.data.network.dto.auth

import com.critx.data.network.dto.SimpleResponseDto
import com.critx.domain.model.Profile

data class ProfileDto(
    val response:SimpleResponseDto,
    val data:ProfileData
)

data class ProfileData(
    val name:String?,
    val username:String?
)

fun ProfileData.asDomain():Profile{
    return Profile(
        name=name.orEmpty(),
        username = username.orEmpty()
    )
}