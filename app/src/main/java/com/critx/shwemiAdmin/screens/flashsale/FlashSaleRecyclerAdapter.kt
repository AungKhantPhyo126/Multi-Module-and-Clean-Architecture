package com.critx.shwemiAdmin.screens.flashsale

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemDiscountBinding
import com.critx.shwemiAdmin.databinding.ItemStockCodeBinding
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel
import com.critx.shwemiAdmin.uiModel.discount.DiscountUIModel

class FlashSaleRecyclerAdapter(private val onclick:(data:CollectStockBatchUIModel)->Unit) : ListAdapter<CollectStockBatchUIModel, FlashSaleViewHolder>(
    FlashSaleDiffUtil
)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashSaleViewHolder {
        return FlashSaleViewHolder(
            ItemStockCodeBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ),onclick)
    }

    override fun onBindViewHolder(holder: FlashSaleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class FlashSaleViewHolder(private val binding: ItemStockCodeBinding,
                         private val onclick: (data:CollectStockBatchUIModel) -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(data: CollectStockBatchUIModel){
        binding.tvStockCodeNumber.text = data.productCode
        binding.ibCross.setOnClickListener {
            onclick(data)
        }
    }
}

object FlashSaleDiffUtil : DiffUtil.ItemCallback<CollectStockBatchUIModel>() {
    override fun areItemsTheSame(oldItem: CollectStockBatchUIModel, newItem: CollectStockBatchUIModel): Boolean {
        return oldItem.productId == newItem.productId
    }

    override fun areContentsTheSame(oldItem: CollectStockBatchUIModel, newItem: CollectStockBatchUIModel): Boolean {
        return oldItem == newItem
    }

}