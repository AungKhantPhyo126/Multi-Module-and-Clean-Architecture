package com.critx.shwemiAdmin.screens.transferCheckUpStock.checkup.checkUpResult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemStockCodeBinding
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel

class CheckUpResultRecyclerAdapter (private val onclick:(item: String)->Unit) :
    ListAdapter<String, StockCodeListViewHolder>(
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
                              private val onclick: (item: String) -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(data: String){
        binding.tvStockCodeNumber.text = data
        binding.ibCross.setOnClickListener {
            onclick(data)
        }
    }
}


object StockCodeListDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}