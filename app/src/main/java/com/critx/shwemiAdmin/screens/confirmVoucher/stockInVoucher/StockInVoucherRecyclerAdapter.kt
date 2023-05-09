package com.critx.shwemiAdmin.screens.confirmVoucher.stockInVoucher

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemStockInVoucherBinding
import com.critx.shwemiAdmin.getGramFromYwae
import com.critx.shwemiAdmin.uiModel.StockInVoucherUiModel
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel

class StockInVoucherRecyclerAdapter() : ListAdapter<StockInVoucherUiModel, StockInVoucherViewHolder>(
    StockInVoucherDiffUtil
)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockInVoucherViewHolder {
        return StockInVoucherViewHolder(
            ItemStockInVoucherBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            ))
    }

    override fun onBindViewHolder(holder: StockInVoucherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class StockInVoucherViewHolder(private val binding: ItemStockInVoucherBinding,
                         ): RecyclerView.ViewHolder(binding.root){
    @SuppressLint("SetTextI18n")
    fun bind(data: StockInVoucherUiModel){
        binding.tvStockCode.text = data.stockCode
        binding.tvWeightDifference.text = getGramFromYwae(data.weightDifferenceYwae.let {
            if (it.isNullOrEmpty()) 0.0 else it.toDouble()
        }).toString() + " gm"
        binding.tvReason.text = data.reason
    }
}

object StockInVoucherDiffUtil : DiffUtil.ItemCallback<StockInVoucherUiModel>() {
    override fun areItemsTheSame(oldItem: StockInVoucherUiModel, newItem: StockInVoucherUiModel): Boolean {
        return oldItem.stockCode == newItem.stockCode
    }

    override fun areContentsTheSame(oldItem: StockInVoucherUiModel, newItem: StockInVoucherUiModel): Boolean {
        return oldItem == newItem
    }

}