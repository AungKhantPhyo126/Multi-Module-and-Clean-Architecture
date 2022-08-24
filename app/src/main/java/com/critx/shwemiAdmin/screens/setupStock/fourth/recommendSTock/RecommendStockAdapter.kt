package com.critx.shwemiAdmin.screens.setupStock.fourth.recommendSTock

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.common.ui.loadImageWithGlide
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.BubbleCardBinding
import com.critx.shwemiAdmin.databinding.ItemAddNewBinding
import com.critx.shwemiAdmin.databinding.ItemImageSelectionBinding
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryCategoryUiModel
import com.daasuu.bl.ArrowDirection
import com.daasuu.bl.BubblePopupHelper

class RecommendStockAdapter(
    private val onclick: (id: String) -> Unit,
    private val addNewClick: () -> Unit,

) : ListAdapter<JewelleryCategoryUiModel, RecyclerView.ViewHolder>(
    ChooseGroupDiffUtil
) {
    val addItemViewType = 2
    val itemViewType = 1

    //    init {
//        setHasStableIds(true)
//    }
//    var tracker: SelectionTracker<Long>? = null
    override fun getItemViewType(position: Int): Int {

        return if (position == itemCount-1) addItemViewType
        else itemViewType;
    }

    override fun getItemCount(): Int {
        return super.getItemCount()+1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == itemViewType) {
            ImageViewHolder(
                ItemImageSelectionBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), onclick,
            )
        } else {
            AddItemViewHolder(
                ItemAddNewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), addNewClick
            )
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> {
                holder.bind(getItem(position))
            }
            is AddItemViewHolder -> {
                holder.bind()
            }

        }
    }


}

class AddItemViewHolder(
    private val binding: ItemAddNewBinding,
    private val addNewClick: () -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        binding.root.setOnClickListener {
            addNewClick()
        }
    }
}

class ImageViewHolder(
    private val binding: ItemImageSelectionBinding,
    private val onclick: (id: String) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: JewelleryCategoryUiModel) {
        binding.ivImage.loadImageWithGlide(data.imageUrl)
        binding.mcvImageCard.setOnClickListener {
            onclick(data.id)
        }
        binding.mcvImageCard.isChecked = data.isChecked
    }

    fun getItem(): ItemDetailsLookup.ItemDetails<Long> =

        //1
        object : ItemDetailsLookup.ItemDetails<Long>() {

            //2
            override fun getPosition(): Int = adapterPosition

            //3
            override fun getSelectionKey(): Long = itemId
        }

}

object ChooseGroupDiffUtil : DiffUtil.ItemCallback<JewelleryCategoryUiModel>() {
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