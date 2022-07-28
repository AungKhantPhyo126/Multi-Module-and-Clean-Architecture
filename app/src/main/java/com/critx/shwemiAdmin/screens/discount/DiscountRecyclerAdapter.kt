package com.critx.shwemiAdmin.screens.discount

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemDiscountBinding
import com.critx.shwemiAdmin.uiModel.discount.DiscountUIModel
import com.critx.shwemiAdmin.uiModel.sampleTakeAndReturn.NewSampleUIModel

class DiscountRecyclerAdapter(private val onclick:()->Unit) : ListAdapter<DiscountUIModel, DiscountViewHolder>(
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
    fun bind(data: DiscountUIModel){
        binding.tvInvoiceCodeNumber.text = data.invoiceCode
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