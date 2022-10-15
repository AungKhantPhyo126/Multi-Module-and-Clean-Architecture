package com.critx.shwemiAdmin.uiModel.checkUpTransfer

import android.os.Parcelable
import com.critx.domain.model.transferCheckUP.CheckUpDomain
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CheckUpUiModel(
    val required:List<String>,
    val notFromBox:List<String>
):Parcelable

fun CheckUpDomain.asUIModel():CheckUpUiModel{
    return  CheckUpUiModel(
        required, notFromBox
    )
}
