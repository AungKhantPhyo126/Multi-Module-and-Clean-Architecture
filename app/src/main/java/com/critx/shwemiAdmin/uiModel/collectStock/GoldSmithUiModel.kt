package com.critx.shwemiAdmin.uiModel.collectStock

import android.os.Parcelable
import com.critx.domain.model.collectStock.GoldSmithListDomain
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GoldSmithUiModel(
    val id:String,
    val name:String
):Parcelable

fun GoldSmithListDomain.asUiModel():GoldSmithUiModel{
    return GoldSmithUiModel(
        id = id,
        name = name
    )
}
