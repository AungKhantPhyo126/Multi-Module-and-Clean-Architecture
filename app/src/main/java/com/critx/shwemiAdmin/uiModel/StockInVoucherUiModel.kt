package com.critx.shwemiAdmin.uiModel

import android.os.Parcelable
import com.critx.domain.model.StockInVoucherDomain
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StockInVoucherUiModel(
    val stockCode:String,
    val weightDifferenceYwae:String?,
    val reason:String?
):Parcelable

fun StockInVoucherDomain.asUiModel():StockInVoucherUiModel{
    return StockInVoucherUiModel(stockCode = code.orEmpty(), weightDifferenceYwae = gold_weight_diff_ywae.orEmpty(),
    reason =reason.orEmpty())
}
