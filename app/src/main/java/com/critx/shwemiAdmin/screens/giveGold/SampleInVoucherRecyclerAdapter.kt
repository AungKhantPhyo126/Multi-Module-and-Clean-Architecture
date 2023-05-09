package com.critx.shwemiAdmin.screens.giveGold

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.common.ui.loadImageWithGlide
import com.critx.domain.model.repairStock.JobDomain
import com.critx.shwemiAdmin.databinding.ItemJobBinding
import com.critx.shwemiAdmin.databinding.ItemSampleInVoucherBinding
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel

class SampleInVoucherRecyclerAdapter(private val checkItem:(sampleId:String,check:Boolean)->Unit) : ListAdapter<SampleItemUIModel, SampleInVoucherViewHolder>(
    SampleInVoucherDiffUtil
)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleInVoucherViewHolder {
        return SampleInVoucherViewHolder(
            ItemSampleInVoucherBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ),checkItem)
    }

    override fun onBindViewHolder(holder: SampleInVoucherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SampleInVoucherViewHolder(private val binding: ItemSampleInVoucherBinding,
                                private val checkItem:(sampleId:String,check:Boolean)->Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(data: SampleItemUIModel){
        binding.ivSample.loadImageWithGlide(data.thumbnail)
        binding.tvSampleInfo.text = data.productCode?:data.name?:"Outside"
        binding.mcvSample.isChecked = data.isChecked
        binding.mcvSample.setOnClickListener {
            binding.mcvSample.isChecked = !binding.mcvSample.isChecked
        }
        binding.mcvSample.setOnCheckedChangeListener { card, isChecked ->
            checkItem(data.id!!,isChecked)
        }
    }
}

object SampleInVoucherDiffUtil : DiffUtil.ItemCallback<SampleItemUIModel>() {
    override fun areItemsTheSame(oldItem: SampleItemUIModel, newItem: SampleItemUIModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SampleItemUIModel, newItem: SampleItemUIModel): Boolean {
        return oldItem == newItem
    }

}