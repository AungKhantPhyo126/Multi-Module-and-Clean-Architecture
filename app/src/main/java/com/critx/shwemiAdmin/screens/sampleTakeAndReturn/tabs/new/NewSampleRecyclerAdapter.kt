package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.new

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemNewSampleBinding
import com.critx.shwemiAdmin.uiModel.sampleTakeAndReturn.NewSampleUIModel

class NewSampleRecyclerAdapter(private val onclick:()->Unit) : ListAdapter<NewSampleUIModel, NewSampleViewHolder>(
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
    fun bind(data:NewSampleUIModel){
        binding.tvStockCodeNumber.text = data.stockNumber.toString()
        binding.tieSpecification.setText(data.specification)
    }
}

object NewSampleDiffUtil : DiffUtil.ItemCallback<NewSampleUIModel>() {
    override fun areItemsTheSame(oldItem: NewSampleUIModel, newItem: NewSampleUIModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NewSampleUIModel, newItem: NewSampleUIModel): Boolean {
        return oldItem == newItem
    }

}