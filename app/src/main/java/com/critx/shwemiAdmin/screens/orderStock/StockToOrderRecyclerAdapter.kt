package com.critx.shwemiAdmin.screens.orderStock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.common.ui.loadImageWithGlide
import com.critx.domain.model.orderStock.BookMarkStockDomain
import com.critx.shwemiAdmin.databinding.ItemStockCodeBinding
import com.critx.shwemiAdmin.databinding.ItemStockToOrderBinding

class StockToOrderRecyclerAdapter (private val onclick:()->Unit,private val orderClick:()->Unit) :
    PagingDataAdapter<BookMarkStockDomain, StockCodeListViewHolder>(
    StockCodeListDiffUtil
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockCodeListViewHolder {
        return StockCodeListViewHolder(
            ItemStockToOrderBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ),onclick,orderClick)
    }

    override fun onBindViewHolder(holder: StockCodeListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class StockCodeListViewHolder(private val binding: ItemStockToOrderBinding,
                              private val onclick: () -> Unit,
                              private val orderClick:()->Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(data: BookMarkStockDomain){
        binding.root.setOnClickListener {
            onclick()
        }
        binding.ivStock.loadImageWithGlide(data.image.url)
        binding.tvKyatValue.text = data.avg_weight_per_unit_kyat
        binding.tvPaeValue.text = data.avg_weight_per_unit_pae
        binding.tvYwaeValue.text = data.avg_weight_per_unit_ywae
    }
}


object StockCodeListDiffUtil : DiffUtil.ItemCallback<BookMarkStockDomain>() {
    override fun areItemsTheSame(oldItem: BookMarkStockDomain, newItem: BookMarkStockDomain): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookMarkStockDomain, newItem: BookMarkStockDomain): Boolean {
        return oldItem == newItem
    }

}