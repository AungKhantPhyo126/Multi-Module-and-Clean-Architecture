package com.critx.shwemiAdmin.uiModel.orderStock

import android.os.Parcelable
import com.critx.domain.model.orderStock.BookMarkStockDomain
import com.critx.domain.model.sampleTakeAndReturn.FileShweMiDomain
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookMarkStockUiModel(
    val id: String,
    val image: String,
    val avg_weight_per_unit_kyat: String,
    val avg_weight_per_unit_pae: String,
    val avg_weight_per_unit_ywae: String,
    val jewellery_type_id: String,
):Parcelable

fun BookMarkStockDomain.asUiModel():BookMarkStockUiModel{
    return BookMarkStockUiModel(
        id, image = image.url, avg_weight_per_unit_kyat, avg_weight_per_unit_pae, avg_weight_per_unit_ywae, jewellery_type_id
    )
}
