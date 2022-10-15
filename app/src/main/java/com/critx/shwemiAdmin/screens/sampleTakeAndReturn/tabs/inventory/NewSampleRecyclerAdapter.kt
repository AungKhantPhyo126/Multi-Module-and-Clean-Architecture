package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.critx.common.ui.loadImageWithGlide
import com.critx.shwemiAdmin.databinding.ItemNewSampleBinding
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel

class NewSampleRecyclerAdapter(private val onclick:()->Unit) : ListAdapter<SampleItemUIModel, NewSampleViewHolder>(
    NewSampleDiffUtil
)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewSampleViewHolder {
        return NewSampleViewHolder(ItemNewSampleBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ),onclick)
    }

    override fun onBindViewHolder(holder: NewSampleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class NewSampleViewHolder(private val binding:ItemNewSampleBinding,
private val onclick: () -> Unit):RecyclerView.ViewHolder(binding.root){
    fun bind(data:SampleItemUIModel){
        binding.ivSample.loadImageWithGlide(data.imageUrl)
        binding.tvStockCodeNumber.text = data.stockCode
        binding.tieSpecification.setText(data.specification)
        binding.tieSpecification.isFocusable = data.specification != null
    }
}

object NewSampleDiffUtil : DiffUtil.ItemCallback<SampleItemUIModel>() {
    override fun areItemsTheSame(oldItem: SampleItemUIModel, newItem: SampleItemUIModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SampleItemUIModel, newItem: SampleItemUIModel): Boolean {
        return oldItem == newItem
    }

}