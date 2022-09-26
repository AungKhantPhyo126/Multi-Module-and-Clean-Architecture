package com.critx.shwemiAdmin.screens.checkUpBoxWeight

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemBoxWeightBinding
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel

class CheckUpBoxRecyclerAdapter :
    ListAdapter<StockCodeForListUiModel, CheckUpBoxViewHolder>(
        CheckUpBoxDiffUtil
    ){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckUpBoxViewHolder {
        return CheckUpBoxViewHolder(
            ItemBoxWeightBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ))
    }

    override fun onBindViewHolder(holder: CheckUpBoxViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CheckUpBoxViewHolder(private val binding: ItemBoxWeightBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(data: StockCodeForListUiModel){
        binding.tvStockCodeNumber.text = data.invoiceCode
    }
}


object CheckUpBoxDiffUtil : DiffUtil.ItemCallback<StockCodeForListUiModel>() {
    override fun areItemsTheSame(oldItem: StockCodeForListUiModel, newItem: StockCodeForListUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: StockCodeForListUiModel, newItem: StockCodeForListUiModel): Boolean {
        return oldItem == newItem
    }

}