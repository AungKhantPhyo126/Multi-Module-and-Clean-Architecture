package com.critx.shwemiAdmin.screens.arrangeBox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemArrangeBoxBinding
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel

class ArrangeBoxRecyclerAdapter :
    ListAdapter<StockCodeForListUiModel, ArrangeBoxViewHolder>(
    ArrangeBoxDiffUtil
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArrangeBoxViewHolder {
        return ArrangeBoxViewHolder(
            ItemArrangeBoxBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ))
    }

    override fun onBindViewHolder(holder: ArrangeBoxViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ArrangeBoxViewHolder(private val binding: ItemArrangeBoxBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(data: StockCodeForListUiModel){
        binding.tvStockCodeNumber.text = data.invoiceCode
    }
}


object ArrangeBoxDiffUtil : DiffUtil.ItemCallback<StockCodeForListUiModel>() {
    override fun areItemsTheSame(oldItem: StockCodeForListUiModel, newItem: StockCodeForListUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: StockCodeForListUiModel, newItem: StockCodeForListUiModel): Boolean {
        return oldItem == newItem
    }

}