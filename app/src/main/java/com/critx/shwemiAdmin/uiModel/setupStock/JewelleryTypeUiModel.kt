package com.critx.shwemiAdmin.uiModel.setupStock

import android.os.Parcelable
import com.critx.domain.model.SetupStock.JewelleryType.JewelleryType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JewelleryTypeUiModel(
    val id:String,
    val name:String
):Parcelable
fun JewelleryType.asUiModel():JewelleryTypeUiModel{
    return JewelleryTypeUiModel(
        id = id,
        name= name
    )
}