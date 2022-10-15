package com.critx.shwemiAdmin.screens.checkUpBoxWeight

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemBoxWeightBinding
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel
import com.critx.shwemiAdmin.uiModel.checkupBoxWeight.BoxWeightUiModel

class CheckUpBoxRecyclerAdapter :
    ListAdapter<BoxWeightUiModel, CheckUpBoxViewHolder>(
        CheckUpBoxDiffUtil
    ){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckUpBoxViewHolder {
        return CheckUpBoxViewHolder(
            ItemBoxWeightBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ))
    }

    override fun onBindViewHolder(holder: CheckUpBoxViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CheckUpBoxViewHolder(private val binding: ItemBoxWeightBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(data: BoxWeightUiModel){
        binding.tvBoxCode.text = data.code
        binding.tvWeightValue.text = data.weight
    }
}


object CheckUpBoxDiffUtil : DiffUtil.ItemCallback<BoxWeightUiModel>() {
    override fun areItemsTheSame(oldItem: BoxWeightUiModel, newItem: BoxWeightUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BoxWeightUiModel, newItem: BoxWeightUiModel): Boolean {
        return oldItem == newItem
    }

}