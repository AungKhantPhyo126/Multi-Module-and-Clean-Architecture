package com.critx.shwemiAdmin.screens.orderStock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemStockCodeBinding
import com.critx.shwemiAdmin.databinding.ItemStockToOrderBinding
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel

class StockToOrderRecyclerAdapter (private val onclick:()->Unit,private val orderClick:()->Unit) :
    ListAdapter<StockCodeForListUiModel, StockCodeListViewHolder>(
    StockCodeListDiffUtil
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockCodeListViewHolder {
        return StockCodeListViewHolder(
            ItemStockToOrderBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ),onclick,orderClick)
    }

    override fun onBindViewHolder(holder: StockCodeListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class StockCodeListViewHolder(private val binding: ItemStockToOrderBinding,
                              private val onclick: () -> Unit,
                              private val orderClick:()->Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(data: StockCodeForListUiModel){
        binding.root.setOnClickListener {
            onclick()
        }
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