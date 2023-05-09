package com.critx.shwemiAdmin.uiModel.orderStock

import android.os.Parcelable
import com.critx.domain.model.orderStock.BookMarkStockDomain
import com.critx.domain.model.orderStock.BookMarkStockInfoDomain
import com.critx.domain.model.sampleTakeAndReturn.FileShweMiDomain
import com.critx.shwemiAdmin.uiModel.collectStock.GoldSmithUiModel
import com.critx.shwemiAdmin.uiModel.collectStock.asUiModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookMarkStockUiModel(
    val id: String,
    val image: String,
    val avg_unit_weight_ywae: String,
    val jewellery_type_id: String,
    val custom_category_name:String,
    val sizes:List<SizeInfoUiModel>,
    val is_orderable:Boolean,
    val goldSmith:GoldSmithUiModel?,
    val goldQuality:String?,
    val isFromCloud:Boolean
):Parcelable

@Parcelize
data class SizeInfoUiModel(
    val id:String,
    val size:String,
    val stock:String
):Parcelable

fun BookMarkStockInfoDomain.asUiMoel():SizeInfoUiModel{
    return SizeInfoUiModel(
        id, size, stock
    )
}

fun BookMarkStockDomain.asUiModel():BookMarkStockUiModel{
    return BookMarkStockUiModel(
        id, image = image.url,avg_unit_weight_ywae, jewellery_type_id,custom_category_name,sizes.map { it.asUiMoel() },is_orderable,goldSmith?.asUiModel(),goldQuality,isFromCloud
    )
}
