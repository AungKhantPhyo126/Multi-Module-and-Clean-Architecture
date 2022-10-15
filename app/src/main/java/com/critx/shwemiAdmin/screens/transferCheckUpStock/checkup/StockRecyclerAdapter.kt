package com.critx.shwemiAdmin.screens.transferCheckUpStock.checkup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemStockCodeBinding
import com.critx.shwemiAdmin.screens.flashsale.FlashSaleViewHolder
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel

class StockRecyclerAdapter (private val onclick:(item:StockCodeForListUiModel)->Unit) :ListAdapter<StockCodeForListUiModel,StockCodeListViewHolder>(
    StockCodeListDiffUtil
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockCodeListViewHolder {
        return StockCodeListViewHolder(
            ItemStockCodeBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ),onclick)
    }

    override fun onBindViewHolder(holder: StockCodeListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class StockCodeListViewHolder(private val binding: ItemStockCodeBinding,
                          private val onclick: (item:StockCodeForListUiModel) -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(data: StockCodeForListUiModel){
        binding.tvStockCodeNumber.text = data.invoiceCode
        binding.ibCross.setOnClickListener {
            onclick(data)
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