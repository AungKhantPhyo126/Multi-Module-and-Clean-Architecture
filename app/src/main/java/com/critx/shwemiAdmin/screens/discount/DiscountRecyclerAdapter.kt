package com.critx.shwemiAdmin.screens.discount

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.domain.model.DiscountVoucherScanDomain
import com.critx.shwemiAdmin.databinding.ItemDiscountBinding
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel

class DiscountRecyclerAdapter(private val onclick:()->Unit) : ListAdapter<DiscountVoucherScanDomain, DiscountViewHolder>(
    DiscountDiffUtil
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
    fun bind(data: DiscountVoucherScanDomain){
        binding.tvInvoiceCodeNumber.text = data.code
    }
}

object DiscountDiffUtil : DiffUtil.ItemCallback<DiscountVoucherScanDomain>() {
    override fun areItemsTheSame(oldItem: DiscountVoucherScanDomain, newItem: DiscountVoucherScanDomain): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DiscountVoucherScanDomain, newItem: DiscountVoucherScanDomain): Boolean {
        return oldItem == newItem
    }

}