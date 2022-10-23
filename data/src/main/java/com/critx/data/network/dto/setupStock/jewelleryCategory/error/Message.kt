package com.critx.data.network.dto.setupStock.jewelleryCategory.error

data class Message(
    val avg_wastage_per_unit_kpy: List<String?>?,
    val avg_weight_per_unit_gm: List<String?>?,
    val designs: List<String?>?,
    val group_id: List<String?>?,
    val images:ImagesError?,
    val jewellery_quality_id: List<String?>?,
    val jewellery_type_id: List<String?>?,
    val name: List<String?>?,
    val order_to_goldsmith: List<String?>?,
    val specification: List<String?>?,
    val video: List<String?>?,
    val related_categories:CategoryError?,
    val product_code:List<String?>?
)

class ImagesError:ArrayList<String?>()
class CategoryError:ArrayList<String?>()

fun Message.getMessage():List<String?>{
    return avg_wastage_per_unit_kpy ?:avg_weight_per_unit_gm?:designs?:group_id?:images?:jewellery_quality_id?:jewellery_type_id?:name?:
    order_to_goldsmith?:specification?:video?:related_categories?:product_code?: emptyList()
}