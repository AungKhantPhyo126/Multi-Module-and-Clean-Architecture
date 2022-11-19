package com.critx.shwemiAdmin.screens.orderStock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.common.ui.loadImageWithGlide
import com.critx.shwemiAdmin.databinding.ItemStockCodeBinding
import com.critx.shwemiAdmin.databinding.ItemStockToOrderBinding
import com.critx.shwemiAdmin.uiModel.orderStock.BookMarkStockUiModel

class StockToOrderRecyclerAdapter (private val orderClick:(data:BookMarkStockUiModel)->Unit) :
    PagingDataAdapter<BookMarkStockUiModel, StockCodeListViewHolder>(
    StockCodeListDiffUtil
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockCodeListViewHolder {
        return StockCodeListViewHolder(
            ItemStockToOrderBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ),orderClick)
    }

    override fun onBindViewHolder(holder: StockCodeListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class StockCodeListViewHolder(private val binding: ItemStockToOrderBinding,
                              private val onclick: (data:BookMarkStockUiModel) -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(data: BookMarkStockUiModel){
        binding.root.setOnClickListener {
            onclick(data)
        }
        binding.ivStock.loadImageWithGlide(data.image)
        binding.tvKyatValue.text = data.avg_weight_per_unit_kyat
        binding.tvPaeValue.text = data.avg_weight_per_unit_pae
        binding.tvYwaeValue.text = data.avg_weight_per_unit_ywae.toDouble().toInt().toString()
    }
}


object StockCodeListDiffUtil : DiffUtil.ItemCallback<BookMarkStockUiModel>() {
    override fun areItemsTheSame(oldItem: BookMarkStockUiModel, newItem: BookMarkStockUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookMarkStockUiModel, newItem: BookMarkStockUiModel): Boolean {
        return oldItem == newItem
    }

}