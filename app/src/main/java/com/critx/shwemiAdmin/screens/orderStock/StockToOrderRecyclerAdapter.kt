package com.critx.shwemiAdmin.screens.orderStock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.common.ui.loadImageWithGlide
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.ItemStockCodeBinding
import com.critx.shwemiAdmin.databinding.ItemStockToOrderBinding
import com.critx.shwemiAdmin.getKPYFromYwae
import com.critx.shwemiAdmin.uiModel.orderStock.BookMarkStockUiModel

class StockToOrderRecyclerAdapter(
    private val orderClick: (data: BookMarkStockUiModel) -> Unit,
    private val imageClick: (url: String) -> Unit
) :
    PagingDataAdapter<BookMarkStockUiModel, StockCodeListViewHolder>(
        StockCodeListDiffUtil
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockCodeListViewHolder {
        return StockCodeListViewHolder(
            ItemStockToOrderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), imageClick,orderClick,
        )
    }

    override fun onBindViewHolder(holder: StockCodeListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class StockCodeListViewHolder(
    private val binding: ItemStockToOrderBinding,
    private val imageClick: (url: String) -> Unit,
    private val onclick: (data: BookMarkStockUiModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: BookMarkStockUiModel) {
        binding.root.setOnClickListener {
            onclick(data)
        }
        binding.ivStock.setOnClickListener {
            imageClick(data.image)
        }
        if (data.isFromCloud){
            binding.mcvOrderStock.strokeColor = binding.root.context.getColor(R.color.primary_color)
        }else{
            binding.mcvOrderStock.strokeColor = binding.root.context.getColor(R.color.green)
        }
        if (data.is_orderable) {
            binding.btnTapToOrder.text = "Tap to Order"
            binding.btnTapToOrder.setTextColor(binding.root.context.getColorStateList(R.color.edit_text_color))
            binding.btnTapToOrder.iconTint =
                binding.root.context.getColorStateList(R.color.edit_text_color)

        } else {
            binding.btnTapToOrder.text = "Ordered"
            binding.btnTapToOrder.iconTint =
                binding.root.context.getColorStateList(R.color.primary_color)
            binding.btnTapToOrder.setTextColor(binding.root.context.getColorStateList(R.color.primary_color))

        }
        binding.tvCatName.text = data.custom_category_name
        binding.ivStock.loadImageWithGlide(data.image)
        val kpyList = getKPYFromYwae(data.avg_unit_weight_ywae.toDouble())
        binding.tvKyatValue.text = kpyList[0].toInt().toString()
        binding.tvPaeValue.text = kpyList[1].toInt().toString()
        binding.tvYwaeValue.text = kpyList[2].toString()
    }
}


object StockCodeListDiffUtil : DiffUtil.ItemCallback<BookMarkStockUiModel>() {
    override fun areItemsTheSame(
        oldItem: BookMarkStockUiModel,
        newItem: BookMarkStockUiModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: BookMarkStockUiModel,
        newItem: BookMarkStockUiModel
    ): Boolean {
        return oldItem == newItem
    }

}