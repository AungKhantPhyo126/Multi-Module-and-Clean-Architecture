package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.handedList

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.common.ui.loadImageWithGlide
import com.critx.domain.model.sampleTakeAndReturn.HandedListDomain
import com.critx.shwemiAdmin.databinding.ItemHandedListBinding
import com.critx.shwemiAdmin.screens.transferCheckUpStock.transfer.matchCode.MatchCodeViewModel

class HandedListRecyclerAdapter(private val onclick:(data: HandedListDomain)->Unit) :
    ListAdapter<HandedListDomain, StockCodeListViewHolder>(
        StockCodeListDiffUtil
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockCodeListViewHolder {
        return StockCodeListViewHolder(
            ItemHandedListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),onclick
        )
    }

    override fun onBindViewHolder(holder: StockCodeListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class StockCodeListViewHolder(
    private val binding: ItemHandedListBinding,
    private val onclick:(data: HandedListDomain)->Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: HandedListDomain) {
        binding.tvStockName.text = data.name
        binding.ivSample.loadImageWithGlide(data.file.url)
        binding.tvSampleSpec.text = data.specification
        if (data.weight.isNotEmpty()){
            binding.tvSampleWeight.text = data.weight + "gm"
        }
        binding.ibCross.setOnClickListener {
            onclick(data)
        }
    }
}


object StockCodeListDiffUtil : DiffUtil.ItemCallback<HandedListDomain>() {
    override fun areItemsTheSame(oldItem: HandedListDomain, newItem: HandedListDomain): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: HandedListDomain, newItem: HandedListDomain): Boolean {
        return oldItem == newItem
    }

}