package com.critx.data.network.dto.sampleTakeAndReturn

import com.critx.domain.model.sampleTakeAndReturn.FileShweMiDomain
import com.critx.domain.model.sampleTakeAndReturn.VoucherSampleDomain

data class VoucherSampleResponse(
    val data:List<VoucherSampleDto>
)

data class VoucherSampleDto(
    val id:String?,
    val sale_id:String?,
    val product_id:String?,
    val quantity:String?,
    val specification:String?,
    val name:String?,
    val weight_gm:String?,
    val type:String?,
    val file:FileShweMiDto?
)

data class FileShweMiDto(
    val id:String?,
    val type: String?,
    val url:String?
)

fun FileShweMiDto.asDomain():FileShweMiDomain{
    return  FileShweMiDomain(
        id = id.orEmpty(),
        type = type.orEmpty(),
        url = url.orEmpty()
    )
}

fun VoucherSampleDto.asDomain():VoucherSampleDomain{
    return VoucherSampleDomain(
        id = id.orEmpty(),
        sale_id=sale_id.orEmpty(),
        product_id = product_id.orEmpty(),
        quantity = quantity.orEmpty(),
        specification =specification.orEmpty(),
        name =name.orEmpty(),
        weight_gm = weight_gm.orEmpty(),
        type=type.orEmpty(),
        file =file!!.asDomain()
    )
}