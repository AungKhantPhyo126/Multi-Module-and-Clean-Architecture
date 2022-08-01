package com.critx.shwemiAdmin.screens.collectstock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemCollectStockBatchBinding
import com.critx.shwemiAdmin.screens.flashsale.NewSampleDiffUtil
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel

class CollectStockRecyclerAdapter(private val onclick:()->Unit) : ListAdapter<CollectStockBatchUIModel, CollectStockViewHolder>(
    CollectStockDiffUtil
)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectStockViewHolder {
        return CollectStockViewHolder(
            ItemCollectStockBatchBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ),onclick)
    }

    override fun onBindViewHolder(holder: CollectStockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class CollectStockViewHolder(private val binding: ItemCollectStockBatchBinding,
                         private val onclick: () -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(data: CollectStockBatchUIModel){
        binding.tvInvoiceCodeNumber.text = data.invoiceCode
    }
}

object CollectStockDiffUtil : DiffUtil.ItemCallback<CollectStockBatchUIModel>() {
    override fun areItemsTheSame(oldItem: CollectStockBatchUIModel, newItem: CollectStockBatchUIModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CollectStockBatchUIModel, newItem: CollectStockBatchUIModel): Boolean {
        return oldItem == newItem
    }

}