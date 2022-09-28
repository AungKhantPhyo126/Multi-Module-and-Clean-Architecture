package com.critx.shwemiAdmin.screens.collectstock.fillinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemCollectStockBatchBinding
import com.critx.shwemiAdmin.databinding.ItemJewellerySizeBinding
import com.critx.shwemiAdmin.uiModel.collectStock.JewellerySizeUIModel

class SizeRecyclerAdapter(private val onclick:(item: JewellerySizeUIModel)->Unit) : ListAdapter<JewellerySizeUIModel, CollectStockViewHolder>(
    SizeDiffUtil
)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectStockViewHolder {
        return CollectStockViewHolder(
            ItemJewellerySizeBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ),onclick)
    }

    override fun onBindViewHolder(holder: CollectStockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class CollectStockViewHolder(private val binding: ItemJewellerySizeBinding,
                             private val onclick: (item: JewellerySizeUIModel) -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(data: JewellerySizeUIModel){
        binding.tvSize.text = data.quantity
        binding.mcvSize.isChecked = data.isChecked
        binding.mcvSize.setOnClickListener {
            onclick(data)
        }
    }
}

object SizeDiffUtil : DiffUtil.ItemCallback<JewellerySizeUIModel>() {
    override fun areItemsTheSame(oldItem: JewellerySizeUIModel, newItem: JewellerySizeUIModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: JewellerySizeUIModel, newItem: JewellerySizeUIModel): Boolean {
        return oldItem == newItem
    }

}