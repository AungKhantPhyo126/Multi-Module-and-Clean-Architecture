package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.common.ui.loadImageWithGlide
import com.critx.domain.model.sampleTakeAndReturn.HandedListDomain
import com.critx.shwemiAdmin.databinding.ItemSampleReturnInventoryBinding
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel

class SampleReturnRecyclerAdapter(private val onclick:(data: SampleItemUIModel)->Unit) :
    ListAdapter<SampleItemUIModel, StockCodeListViewHolder>(
        StockCodeListDiffUtil
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockCodeListViewHolder {
        return StockCodeListViewHolder(
            ItemSampleReturnInventoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),onclick
        )
    }

    override fun onBindViewHolder(holder: StockCodeListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class StockCodeListViewHolder(
    private val binding: ItemSampleReturnInventoryBinding,
    private val onclick:(data: SampleItemUIModel)->Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: SampleItemUIModel) {
        binding.ibCross.setOnClickListener {
            onclick(data)
        }
        binding.ivSample.loadImageWithGlide(data.imageUrl)
        binding.tvStockCodeNumber.text = data.productCode

    }
}


object StockCodeListDiffUtil : DiffUtil.ItemCallback<SampleItemUIModel>() {
    override fun areItemsTheSame(oldItem: SampleItemUIModel, newItem: SampleItemUIModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SampleItemUIModel, newItem: SampleItemUIModel): Boolean {
        return oldItem == newItem
    }

}