package com.critx.shwemiAdmin.screens.orderStock.fillOrderInfo

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.common.ui.loadImageWithGlide
import com.critx.shwemiAdmin.databinding.ItemSampleImageBinding
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel

class SampleImageRecyclerAdapter(
    private val removeClick: (itemId: SampleItemUIModel) -> Unit
) :
    ListAdapter<SampleItemUIModel, SampleImageViewHolder>(
        SampleImageDiffUtil
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleImageViewHolder {
        return SampleImageViewHolder(
            ItemSampleImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),removeClick
        )
    }

    override fun onBindViewHolder(holder: SampleImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SampleImageViewHolder(
    private val binding: ItemSampleImageBinding,
    private val removeClick: (itemId: SampleItemUIModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: SampleItemUIModel) {
        binding.mcvRemove.setOnClickListener {
            removeClick(data)
        }
        binding.ivSample.loadImageWithGlide(data.thumbnail)
        if (data.productCode.isNullOrEmpty()) {
            binding.tvSampleInfo.text = "Outside"
        } else {
            binding.tvSampleInfo.text = data.productCode
        }
    }
}


object SampleImageDiffUtil : DiffUtil.ItemCallback<SampleItemUIModel>() {
    override fun areItemsTheSame(
        oldItem: SampleItemUIModel,
        newItem: SampleItemUIModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: SampleItemUIModel,
        newItem: SampleItemUIModel
    ): Boolean {
        return oldItem == newItem
    }

}