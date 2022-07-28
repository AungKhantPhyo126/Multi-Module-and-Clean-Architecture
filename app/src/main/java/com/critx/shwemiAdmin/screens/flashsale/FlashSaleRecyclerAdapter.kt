package com.critx.shwemiAdmin.screens.flashsale

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemDiscountBinding
import com.critx.shwemiAdmin.databinding.ItemFlashSaleBinding
import com.critx.shwemiAdmin.uiModel.discount.DiscountUIModel

class FlashSaleRecyclerAdapter(private val onclick:()->Unit) : ListAdapter<DiscountUIModel, FlashSaleViewHolder>(
    NewSampleDiffUtil
)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashSaleViewHolder {
        return FlashSaleViewHolder(
            ItemFlashSaleBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ),onclick)
    }

    override fun onBindViewHolder(holder: FlashSaleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class FlashSaleViewHolder(private val binding: ItemFlashSaleBinding,
                         private val onclick: () -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(data: DiscountUIModel){
        binding.tvStockCodeNumber.text = data.invoiceCode
    }
}

object NewSampleDiffUtil : DiffUtil.ItemCallback<DiscountUIModel>() {
    override fun areItemsTheSame(oldItem: DiscountUIModel, newItem: DiscountUIModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DiscountUIModel, newItem: DiscountUIModel): Boolean {
        return oldItem == newItem
    }

}