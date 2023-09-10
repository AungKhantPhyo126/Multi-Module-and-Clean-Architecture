package com.critx.shwemiAdmin.screens.confirmVoucher.unconfirmVoucher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.domain.model.voucher.UnConfirmVoucherDomain
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.ItemUnconfirmedVoucherBinding
import com.critx.shwemiAdmin.uiModel.UnConfirmVoucherUIModel
import com.critx.shwemiAdmin.uiModel.sampleTakeAndReturn.VoucherUIModel

class UnConfirmVoucherRecyclerAdapter (private val onclick:(voucherCode:String)->Unit): ListAdapter<UnConfirmVoucherDomain, UnConfirmVoucherViewHolder>(
    VoucherDiffUtil
)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnConfirmVoucherViewHolder {
        return UnConfirmVoucherViewHolder(
            ItemUnconfirmedVoucherBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ),onclick)
    }

    override fun onBindViewHolder(holder: UnConfirmVoucherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class UnConfirmVoucherViewHolder(private val binding: ItemUnconfirmedVoucherBinding,
                                 private val onclick:(voucherCode:String)->Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(data: UnConfirmVoucherDomain){
        binding.root.setOnClickListener {
            onclick(data.code.orEmpty())
        }
        binding.tvInvoiceCode.text = data.code.toString()

        if (data.type == "Pawn"){
            binding.tvBalance.text = data.cost
            binding.tvDeposit.text = "-"
        }else if (data.type == "PrepaidPawnPayment"){
            binding.tvBalance.text = data.cost
            binding.tvDeposit.text = "-"
        }else{
            binding.tvDeposit.text = data.paid_amount.toString()
            binding.tvBalance.text = data.remaining_amount.toString()
        }
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

object VoucherDiffUtil : DiffUtil.ItemCallback<UnConfirmVoucherDomain>() {
    override fun areItemsTheSame(oldItem: UnConfirmVoucherDomain, newItem: UnConfirmVoucherDomain): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: UnConfirmVoucherDomain, newItem: UnConfirmVoucherDomain): Boolean {
        return oldItem == newItem
    }

}