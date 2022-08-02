package com.critx.shwemiAdmin.uiModel.dailygoldandprice

import com.critx.domain.model.Profile
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

data class ProfileUIModel(
    val name:String,
    val todayDate:String,
    val todayName:String
)

fun Profile.asUiModel():ProfileUIModel{
    val dateformatter = DateTimeFormatter.ofPattern("MMMM dd,yyyy")
    val dateNameFormatter = DateTimeFormatter.ofPattern("EEEE")
    return ProfileUIModel(
        name = name,
        todayDate = LocalDate.now().format(dateformatter),
        todayName = LocalDate.now().format(dateNameFormatter)
    )
}
