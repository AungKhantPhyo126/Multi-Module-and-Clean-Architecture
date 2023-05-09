package com.critx.shwemiAdmin.screens.discount

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemDiscountBinding
import com.critx.shwemiAdmin.screens.flashsale.NewSampleDiffUtil
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel

class DiscountRecyclerAdapter(private val onclick:()->Unit) : ListAdapter<CollectStockBatchUIModel, DiscountViewHolder>(
    NewSampleDiffUtil
)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountViewHolder {
        return DiscountViewHolder(
            ItemDiscountBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ),onclick)
    }

    override fun onBindViewHolder(holder: DiscountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class DiscountViewHolder(private val binding: ItemDiscountBinding,
                          private val onclick: () -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(data: CollectStockBatchUIModel){
        binding.tvInvoiceCodeNumber.text = data.productCode
    }
}

object NewSampleDiffUtil : DiffUtil.ItemCallback<CollectStockBatchUIModel>() {
    override fun areItemsTheSame(oldItem: CollectStockBatchUIModel, newItem: CollectStockBatchUIModel): Boolean {
        return oldItem.productId == newItem.productId
    }

    override fun areContentsTheSame(oldItem: CollectStockBatchUIModel, newItem: CollectStockBatchUIModel): Boolean {
        return oldItem == newItem
    }

}