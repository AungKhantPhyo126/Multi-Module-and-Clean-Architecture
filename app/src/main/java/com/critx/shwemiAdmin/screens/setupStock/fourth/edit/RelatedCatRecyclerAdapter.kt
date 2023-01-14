package com.critx.shwemiAdmin.screens.setupStock.fourth.edit

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.common.ui.loadImageWithGlide
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.BubbleCardBinding
import com.critx.shwemiAdmin.databinding.ItemAddNewBinding
import com.critx.shwemiAdmin.databinding.ItemImageSelectionBinding
import com.critx.shwemiAdmin.databinding.ItemRelatedCatBinding
import com.critx.shwemiAdmin.screens.setupStock.fourth.ChooseJewelleryCategoryDiffUtil
import com.critx.shwemiAdmin.screens.setupStock.third.AddItemViewHolder
import com.critx.shwemiAdmin.screens.setupStock.third.ImageViewHolder
import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryCategoryUiModel
import com.daasuu.bl.ArrowDirection
import com.daasuu.bl.BubblePopupHelper

class RelatedCatRecyclerAdapter(
    private val deleteClick: (item: JewelleryCategoryUiModel) -> Unit,
) : ListAdapter<JewelleryCategoryUiModel, RelatedCatViewHolder>(
    RelatedCatDiffUtil
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedCatViewHolder {
        return RelatedCatViewHolder( ItemRelatedCatBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),deleteClick)
    }

    override fun onBindViewHolder(holder: RelatedCatViewHolder, position: Int) {
       holder.bind(getItem(position))
    }

}

class RelatedCatViewHolder(
    private val binding: ItemRelatedCatBinding,
    private val deleteClick: (id: JewelleryCategoryUiModel) -> Unit,

    ) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: JewelleryCategoryUiModel) {
        binding.ivCat.loadImageWithGlide(data.imageUrlList[0])
        binding.tvCatName.text =data.name
        binding.mcvRemove.setOnClickListener {
            deleteClick(data)
        }
    }

}


object RelatedCatDiffUtil : DiffUtil.ItemCallback<JewelleryCategoryUiModel>() {
    override fun areItemsTheSame(
        oldItem: JewelleryCategoryUiModel,
        newItem: JewelleryCategoryUiModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: JewelleryCategoryUiModel,
        newItem: JewelleryCategoryUiModel
    ): Boolean {
        return oldItem == newItem
    }

}