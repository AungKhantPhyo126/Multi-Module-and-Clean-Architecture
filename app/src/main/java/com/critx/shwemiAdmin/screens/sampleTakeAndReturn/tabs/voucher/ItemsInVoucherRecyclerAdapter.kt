package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.voucher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemProductInVoucherBinding
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.StockInVocuherUIModel
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.VoucherForSampleUIModel

class ItemsInVoucherRecyclerAdapter : ListAdapter<StockInVocuherUIModel, ItemsInVoucherViewHolder>(
    ItemsInvoucherDiffUtil
)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsInVoucherViewHolder {
        return ItemsInVoucherViewHolder(
            ItemProductInVoucherBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ),)
    }

    override fun onBindViewHolder(holder: ItemsInVoucherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class ItemsInVoucherViewHolder(private val binding: ItemProductInVoucherBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(data: StockInVocuherUIModel){

    }
}

object ItemsInvoucherDiffUtil : DiffUtil.ItemCallback<StockInVocuherUIModel>() {
    override fun areItemsTheSame(oldItem: StockInVocuherUIModel, newItem: StockInVocuherUIModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: StockInVocuherUIModel, newItem: StockInVocuherUIModel): Boolean {
        return oldItem == newItem
    }

}