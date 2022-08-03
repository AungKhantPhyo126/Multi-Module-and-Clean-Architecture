package com.critx.shwemiAdmin.uiModel.setupStock

import android.os.Parcelable
import com.critx.domain.model.SetupStock.jewelleryQuality.JewelleryQuality
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JewelleryQualityUiModel(
    val id: String,
    val name: String
):Parcelable
fun JewelleryQuality.asUiModel():JewelleryQualityUiModel{
    return JewelleryQualityUiModel(
        id = id ,
        name =name
    )
}