package com.critx.data.network.dto.setupStock.jewelleryCategory.error

data class Message(
    val message:String?,
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
    val product_code:List<String?>?,
    val repair_stocks:List<String?>?,
    val amount:List<String?>?,
    val goldsmith_id:List<String?>?,
    val order_item:List<String?>?,
    val order_qty:List<String?>?,
    val weight_kyat_per_unit:List<String?>?,
    val weight_pae_per_unit:List<String?>?,
    val weight_ywae_per_unit:List<String?>?,
    val gold_box_id:List<String?>?,
    val gold_weight_gm:List<String?>?,
    val gem_weight_gm:List<String?>?,
    val gold_gem_weight_gm:List<String?>?,
    val due_date:List<String?>?,
    val wastage_kyat:List<String?>?,
    val wastage_pae:List<String?>?,
    val wastage_ywae:List<String?>?,
)

class ImagesError:ArrayList<String?>()
class CategoryError:ArrayList<String?>()

fun Message.getMessage():List<String?>{
    return avg_wastage_per_unit_kpy ?:avg_weight_per_unit_gm?:designs?:group_id?:images?:jewellery_quality_id?:jewellery_type_id?:name?:
    order_to_goldsmith?:specification?:video?:related_categories?:product_code?:repair_stocks?:amount?:
    gold_box_id?:order_item?:order_qty?:weight_kyat_per_unit?:weight_pae_per_unit?:weight_ywae_per_unit?:
    gold_box_id?:gold_weight_gm?:gem_weight_gm?:gold_gem_weight_gm?:due_date?:wastage_kyat?:wastage_pae?:wastage_ywae?: listOf(message)?: emptyList()
}