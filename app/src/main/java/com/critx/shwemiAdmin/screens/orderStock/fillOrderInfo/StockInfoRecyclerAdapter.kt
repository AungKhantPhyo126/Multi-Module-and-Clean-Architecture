package com.critx.shwemiAdmin.screens.orderStock.fillOrderInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemStockInfoInFillOrderInfoBinding
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel

class StockInfoRecyclerAdapter  :
    ListAdapter<StockCodeForListUiModel, StockCodeListViewHolder>(
    StockCodeListDiffUtil
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockCodeListViewHolder {
        return StockCodeListViewHolder(
            ItemStockInfoInFillOrderInfoBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ))
    }

    override fun onBindViewHolder(holder: StockCodeListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class StockCodeListViewHolder(private val binding: ItemStockInfoInFillOrderInfoBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(data: StockCodeForListUiModel){

    }
}


object StockCodeListDiffUtil : DiffUtil.ItemCallback<StockCodeForListUiModel>() {
    override fun areItemsTheSame(oldItem: StockCodeForListUiModel, newItem: StockCodeForListUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: StockCodeForListUiModel, newItem: StockCodeForListUiModel): Boolean {
        return oldItem == newItem
    }

}