package com.critx.shwemiAdmin.screens.confirmVoucher.unconfirmVoucher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.ItemUnconfirmedVoucherBinding
import com.critx.shwemiAdmin.databinding.ItemVoucherBinding
import com.critx.shwemiAdmin.uiModel.UnConfirmVoucherUIModel
import com.critx.shwemiAdmin.uiModel.sampleTakeAndReturn.VoucherUIModel

class UnConfirmVoucherRecyclerAdapter : ListAdapter<UnConfirmVoucherUIModel, UnConfirmVoucherViewHolder>(
    VoucherDiffUtil
)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnConfirmVoucherViewHolder {
        return UnConfirmVoucherViewHolder(
            ItemUnconfirmedVoucherBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun onBindViewHolder(holder: UnConfirmVoucherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class UnConfirmVoucherViewHolder(private val binding: ItemUnconfirmedVoucherBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(data: UnConfirmVoucherUIModel){
        binding.tvInvoiceCode.text = data.invoiceCode.toString()
        binding.tvDeposit.text = data.deposit.toString()
        binding.tvBalance.text = data.balance.toString()
        if (adapterPosition%2 ==0){
            binding.rootLayout.setBackgroundColor(binding.root.context.getColor(R.color.white))
            binding.apply {
                dividerOne.setBackgroundColor(binding.root.context.getColor(R.color.base_pink))
                dividerTwo.setBackgroundColor(binding.root.context.getColor(R.color.base_pink))
                dividerThree.setBackgroundColor(binding.root.context.getColor(R.color.base_pink))
                tvInvoiceCode.setBackgroundColor(binding.root.context.getColor(R.color.white))
                tvDeposit.setBackgroundColor(binding.root.context.getColor(R.color.white))
                tvBalance.setBackgroundColor(binding.root.context.getColor(R.color.white))

            }
        }
    }
}

object VoucherDiffUtil : DiffUtil.ItemCallback<UnConfirmVoucherUIModel>() {
    override fun areItemsTheSame(oldItem: UnConfirmVoucherUIModel, newItem: UnConfirmVoucherUIModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UnConfirmVoucherUIModel, newItem: UnConfirmVoucherUIModel): Boolean {
        return oldItem == newItem
    }

}