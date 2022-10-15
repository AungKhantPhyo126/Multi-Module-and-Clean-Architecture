package com.critx.shwemiAdmin.screens.arrangeBox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemArrangeBoxBinding
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel
import com.critx.shwemiAdmin.uiModel.checkUpTransfer.BoxScanUIModel
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel

class ArrangeBoxRecyclerAdapter(private val onclick:(item: BoxScanUIModel)->Unit)  :
    ListAdapter<BoxScanUIModel, ArrangeBoxViewHolder>(
    ArrangeBoxDiffUtil
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArrangeBoxViewHolder {
        return ArrangeBoxViewHolder(
            ItemArrangeBoxBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ),onclick)
    }

    override fun onBindViewHolder(holder: ArrangeBoxViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ArrangeBoxViewHolder(private val binding: ItemArrangeBoxBinding,
                           private val onclick:(item:BoxScanUIModel)->Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(data: BoxScanUIModel){
        binding.tvStockCodeNumber.text = data.code
        binding.ibCross.setOnClickListener {
            onclick(data)
        }
    }
}


object ArrangeBoxDiffUtil : DiffUtil.ItemCallback<BoxScanUIModel>() {
    override fun areItemsTheSame(oldItem: BoxScanUIModel, newItem: BoxScanUIModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BoxScanUIModel, newItem: BoxScanUIModel): Boolean {
        return oldItem == newItem
    }

}