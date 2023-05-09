package com.critx.shwemiAdmin.uiModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StockInVoucherUiModel(
    val id:String,
    val stockCode:String,
    val weightDifference:String?,
    val reason:String?
):Parcelable
