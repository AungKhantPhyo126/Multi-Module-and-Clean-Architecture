package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.outside

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.common.ui.loadImageWithGlide
import com.critx.domain.model.repairStock.JobDomain
import com.critx.domain.model.sampleTakeAndReturn.OutsideSampleDomain
import com.critx.domain.model.sampleTakeAndReturn.VoucherSampleDomain
import com.critx.shwemiAdmin.databinding.ItemSampleReturnOutsideBinding
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel

class SampleReturnInventoryRecyclerAdapter (private val onclick:(id:String)->Unit) : ListAdapter<OutsideSampleDomain, CollectStockViewHolder>(
    CollectStockDiffUtil
)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectStockViewHolder {
        return CollectStockViewHolder(
            ItemSampleReturnOutsideBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ),onclick)
    }

    override fun onBindViewHolder(holder: CollectStockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class CollectStockViewHolder(private val binding: ItemSampleReturnOutsideBinding,
private val onclick:(id:String)->Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(data: OutsideSampleDomain){
       binding.tvLabelStockName.text = data.name
        binding.ivSample.loadImageWithGlide(data.file.url)
        binding.tvWeightValue.text = data.weightGm
        binding.checkBox.isChecked = data.isChecked
        binding.checkBox.setOnClickListener {
            onclick(data.id.toString())
        }
    }
}

object CollectStockDiffUtil : DiffUtil.ItemCallback<OutsideSampleDomain>() {
    override fun areItemsTheSame(oldItem: OutsideSampleDomain, newItem: OutsideSampleDomain): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: OutsideSampleDomain, newItem: OutsideSampleDomain): Boolean {
        return oldItem == newItem
    }

}