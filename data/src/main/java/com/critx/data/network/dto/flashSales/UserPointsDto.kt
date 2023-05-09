package com.critx.data.network.dto.flashSales

import com.critx.domain.model.flashSales.UserPointsDomain


data class UserPointsResponse(
    val data:UserPointsDto
)
data class UserPointsDto(
    val user_name:String?,
    val total_point:String?
)

fun UserPointsDto.asDomain():UserPointsDomain{
    return UserPointsDomain(
        user_name, total_point
    )
}
