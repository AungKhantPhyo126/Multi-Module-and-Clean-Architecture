package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.voucher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemNewSampleBinding
import com.critx.shwemiAdmin.databinding.ItemVoucherBinding
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.new.NewSampleDiffUtil
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.VoucherForSampleUIModel

class VoucherRecyclerAdapter(private val onclick:()->Unit) : ListAdapter<VoucherForSampleUIModel, VoucherViewHolder>(
    VoucherDiffUtil
)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherViewHolder {
        return VoucherViewHolder(ItemVoucherBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ),onclick)
    }

    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class VoucherViewHolder(private val binding:ItemVoucherBinding,
                          private val onclick: () -> Unit):RecyclerView.ViewHolder(binding.root){
    fun bind(data:VoucherForSampleUIModel){
        binding.tvInvoiceCodeNumber.text = data.invoiceCode
        binding.tvQty.text = data.totalQty
        val adapter = ItemsInVoucherRecyclerAdapter()
        binding.rvProductsInVoucher.adapter = adapter
        adapter.submitList(data.stockList)
    }
}

object VoucherDiffUtil : DiffUtil.ItemCallback<VoucherForSampleUIModel>() {
    override fun areItemsTheSame(oldItem: VoucherForSampleUIModel, newItem: VoucherForSampleUIModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: VoucherForSampleUIModel, newItem: VoucherForSampleUIModel): Boolean {
        return oldItem == newItem
    }

}