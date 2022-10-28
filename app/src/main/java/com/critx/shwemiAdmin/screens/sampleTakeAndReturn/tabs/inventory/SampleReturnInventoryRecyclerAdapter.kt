package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.common.ui.loadImageWithGlide
import com.critx.domain.model.repairStock.JobDomain
import com.critx.domain.model.sampleTakeAndReturn.InventorySampleDomain
import com.critx.domain.model.sampleTakeAndReturn.VoucherSampleDomain
import com.critx.shwemiAdmin.databinding.ItemSampleReturnInventoryBinding
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel

class SampleReturnInventoryRecyclerAdapter : ListAdapter<InventorySampleDomain, CollectStockViewHolder>(
    CollectStockDiffUtil
)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectStockViewHolder {
        return CollectStockViewHolder(
            ItemSampleReturnInventoryBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ))
    }

    override fun onBindViewHolder(holder: CollectStockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class CollectStockViewHolder(private val binding: ItemSampleReturnInventoryBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(data: InventorySampleDomain){
//       binding.tvLabelStockName.text = data.name
//        binding.ivSample.loadImageWithGlide(data.file.url)
//        binding.tvWeightValue.text = data.weight_gm
    }
}

object CollectStockDiffUtil : DiffUtil.ItemCallback<InventorySampleDomain>() {
    override fun areItemsTheSame(oldItem: InventorySampleDomain, newItem: InventorySampleDomain): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: InventorySampleDomain, newItem: InventorySampleDomain): Boolean {
        return oldItem == newItem
    }

}