package com.critx.shwemiAdmin.uiModel

import com.critx.domain.model.flashSales.UserPointsDomain

data class UserPointsUiModel(
    val user_name:String?,
    val total_point:String?
)

fun UserPointsDomain.asUiModel():UserPointsUiModel{
    return UserPointsUiModel(user_name, total_point)
}

