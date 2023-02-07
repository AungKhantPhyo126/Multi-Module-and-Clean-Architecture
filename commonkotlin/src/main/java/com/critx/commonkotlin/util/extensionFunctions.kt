package com.critx.commonkotlin.util
fun getOrderValue(kyat:Double,pae:Double,ywae:Double,jewelleryType:String,totalOrderQty:Int):String{
    val kyat =  (kyat + pae / 16 + ywae/128).toInt()
    var result = ""
    when(jewelleryType){
        "A"->{
            result = ((totalOrderQty * kyat* 16)*128/16).toString()
        }
        "B"->{
            result = ((totalOrderQty * kyat* 16)*128/17).toString()

        }
        "C"->{
            result = ((totalOrderQty * kyat* 16)*128/17.5).toString()

        }else->{
        result = ((totalOrderQty * kyat* 16)*128/18).toString()
        }
    }

    return result
}

