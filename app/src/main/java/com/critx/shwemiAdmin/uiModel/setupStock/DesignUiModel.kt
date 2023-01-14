package com.critx.shwemiAdmin.uiModel.setupStock

import android.os.Parcelable
import com.critx.domain.model.SetupStock.jewelleryCategory.DesignDomain
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DesignUiModel(
    val id:String,
    val name:String
):Parcelable
fun DesignDomain.asUiModel():DesignUiModel{
    return DesignUiModel(
        id, name
    )
}

