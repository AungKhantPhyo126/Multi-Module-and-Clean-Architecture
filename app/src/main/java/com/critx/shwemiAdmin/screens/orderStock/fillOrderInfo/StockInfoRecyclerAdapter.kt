package com.critx.shwemiAdmin.screens.orderStock.fillOrderInfo

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.databinding.ItemStockInfoInFillOrderInfoBinding
import com.critx.shwemiAdmin.uiModel.orderStock.SizeInfoUiModel

class StockInfoRecyclerAdapter(private val viewModel: FillOrderInfoViewModel) :
    ListAdapter<SizeInfoUiModel, StockCodeListViewHolder>(
        StockCodeListDiffUtil
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockCodeListViewHolder {
        return StockCodeListViewHolder(
            ItemStockInfoInFillOrderInfoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), viewModel
        )
    }

    override fun onBindViewHolder(holder: StockCodeListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class StockCodeListViewHolder(
    private val binding: ItemStockInfoInFillOrderInfoBinding,
    private val viewModel: FillOrderInfoViewModel
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: SizeInfoUiModel) {
        binding.tvSizeValue.text = data.size
        binding.tvInstockQty.text = data.stock
        binding.edtOrderQty.setText(viewModel.orderQtyList[bindingAdapterPosition])
        binding.edtOrderQty.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.orderQtyList[bindingAdapterPosition] = s.toString()
            }
        })
    }
}


object StockCodeListDiffUtil : DiffUtil.ItemCallback<SizeInfoUiModel>() {
    override fun areItemsTheSame(
        oldItem: SizeInfoUiModel,
        newItem: SizeInfoUiModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: SizeInfoUiModel,
        newItem: SizeInfoUiModel
    ): Boolean {
        return oldItem == newItem
    }

}