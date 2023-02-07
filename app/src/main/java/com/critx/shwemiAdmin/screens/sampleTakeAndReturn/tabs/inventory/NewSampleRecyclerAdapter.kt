package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.inventory

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.critx.common.ui.loadImageWithGlide
import com.critx.shwemiAdmin.databinding.ItemNewSampleBinding
import com.critx.shwemiAdmin.databinding.ItemSavedSampleBinding
import com.critx.shwemiAdmin.screens.setupStock.third.AddItemViewHolder
import com.critx.shwemiAdmin.screens.setupStock.third.ImageViewHolder
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel

class NewSampleRecyclerAdapter(
    private val onclick: (data: SampleItemUIModel) -> Unit,
    private val viewModel: InventoryViewModel
) : ListAdapter<SampleItemUIModel, RecyclerView.ViewHolder>(
    NewSampleDiffUtil
) {

    val unsavedSampleViewType = 2
    val savedSampleViewType = 1
    override fun getItemViewType(position: Int): Int {

        return if (getItem(position).specification.isNullOrEmpty()) unsavedSampleViewType
        else savedSampleViewType;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == unsavedSampleViewType) {
            NewSampleViewHolder(
                ItemNewSampleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), onclick, viewModel
            )
        } else {
            SaveSampleViewHolder(
                ItemSavedSampleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), onclick
            )
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewSampleViewHolder -> {
                holder.bind(getItem(position))
            }
            is SaveSampleViewHolder -> {
                holder.bind(getItem(position))
            }

        }
    }

}

class SaveSampleViewHolder(
    private val binding: ItemSavedSampleBinding,
    private val onclick: (data: SampleItemUIModel) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: SampleItemUIModel) {
        binding.ibCross.setOnClickListener {
            onclick(data)
        }
        binding.ivSample.loadImageWithGlide(data.thumbnail)
        binding.tvStockCodeNumber.text = data.productCode
        binding.tvSampleSpec.text = data.specification
        binding.tvWeightGm.text = data.weight_gm + "gm"
    }
}

class NewSampleViewHolder(
    private val binding: ItemNewSampleBinding,
    private val onclick: (data: SampleItemUIModel) -> Unit,
    private val viewModel: InventoryViewModel
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: SampleItemUIModel) {
        binding.ibCross.setOnClickListener {
            onclick(data)
        }
        binding.ivSample.loadImageWithGlide(data.thumbnail)
        binding.tvStockCodeNumber.text = data.productCode
        binding.tieSpecification.setText(viewModel.specificationList[bindingAdapterPosition])
        binding.tieSpecification.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.specificationList[bindingAdapterPosition] = s.toString()
            }
        })
    }
}

object NewSampleDiffUtil : DiffUtil.ItemCallback<SampleItemUIModel>() {
    override fun areItemsTheSame(oldItem: SampleItemUIModel, newItem: SampleItemUIModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: SampleItemUIModel,
        newItem: SampleItemUIModel
    ): Boolean {
        return oldItem == newItem
    }

}