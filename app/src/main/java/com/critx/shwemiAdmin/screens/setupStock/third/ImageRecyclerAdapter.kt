package com.critx.shwemiAdmin.screens.setupStock.third

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.BubbleCardBinding
import com.critx.shwemiAdmin.databinding.ItemAddNewBinding
import com.critx.shwemiAdmin.databinding.ItemFlashSaleBinding
import com.critx.shwemiAdmin.databinding.ItemImageSelectionBinding
import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.daasuu.bl.ArrowDirection
import com.daasuu.bl.BubblePopupHelper
import kotlinx.coroutines.NonDisposableHandle.parent

class ImageRecyclerAdapter(
    private val onclick: (id: String) -> Unit,
    private val addNewClick: () -> Unit,
    private val navigateToEditClick:()->Unit

) : ListAdapter<ChooseGroupUIModel, RecyclerView.ViewHolder>(
    ChooseGroupDiffUtil
) {
    val addItemViewType = 2
    val itemViewType = 1

    //    init {
//        setHasStableIds(true)
//    }
//    var tracker: SelectionTracker<Long>? = null
    override fun getItemViewType(position: Int): Int {
        return if (position == (itemCount - 1)) addItemViewType
        else itemViewType;
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == itemViewType) {
            ImageViewHolder(
                ItemImageSelectionBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), onclick,navigateToEditClick
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
    private val navigateToEditClick:()->Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: ChooseGroupUIModel) {

        binding.mcvImageCard.setOnClickListener {
            onclick(data.id)
        }
        binding.mcvImageCard.isChecked = data.isChecked
        binding.mcvImageCard.setOnLongClickListener {
            val bubble = BubbleCardBinding.inflate(
                LayoutInflater.from(binding.root.context),
                binding.root,
                false
            ).root
            val editView = bubble.findViewById<ImageView>(R.id.iv_edit)

            val popupWindow: PopupWindow = BubblePopupHelper.create(binding.root.context, bubble)
            popupWindow.width = 300
            popupWindow.height = 200

            val location = IntArray(2)
            binding.mcvImageCard.getLocationInWindow(location)
            bubble.arrowDirection = ArrowDirection.BOTTOM
            popupWindow.showAtLocation(
                binding.mcvImageCard,
                Gravity.NO_GRAVITY,
                location[0],
                location[1] - binding.mcvImageCard.height
            )
            editView.setOnClickListener {
                popupWindow.dismiss()
                navigateToEditClick()
            }
            return@setOnLongClickListener true
        }

//        if (data.isChecked){
//            binding.imageView.setImageDrawable(binding.root.context.getDrawable(com.critx.common.R.drawable.profile_avatar))
//            binding.imageView.foregroundTintList = binding.root.context.getColorStateList(R.color.primary_color)
//        }else{
//            binding.imageView.foregroundTintList = binding.root.context.getColorStateList(R.color.white)
//        }
//        binding.root.setOnClickListener {
//            data.isChecked = !data.isChecked
//
//        }

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

object ChooseGroupDiffUtil : DiffUtil.ItemCallback<ChooseGroupUIModel>() {
    override fun areItemsTheSame(
        oldItem: ChooseGroupUIModel,
        newItem: ChooseGroupUIModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ChooseGroupUIModel,
        newItem: ChooseGroupUIModel
    ): Boolean {
        return oldItem == newItem
    }

}