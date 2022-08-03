package com.critx.shwemiAdmin.screens.setupStock.third

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class ItemsDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
 
 //2
 override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
   //3
   val view = recyclerView.findChildViewUnder(event.x, event.y)
   if (view != null) {
   //4
     return (recyclerView.getChildViewHolder(view) as ImageViewHolder).getItem()
   }
   return null
 }
}