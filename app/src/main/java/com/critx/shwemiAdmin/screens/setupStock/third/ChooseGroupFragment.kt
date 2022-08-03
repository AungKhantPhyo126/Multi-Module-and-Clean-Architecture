package com.critx.shwemiAdmin.screens.setupStock.third

import com.critx.shwemiAdmin.screens.setupStock.first.SetupStockFragmentDirections
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import com.critx.common.databinding.ChoiceChipBinding
import com.critx.common.ui.createChip
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.BubbleCardBinding
import com.critx.shwemiAdmin.databinding.FragmentChooseGroupBinding
import com.critx.shwemiAdmin.databinding.ItemImageSelectionBinding
import com.critx.shwemiAdmin.uiModel.discount.DiscountUIModel
import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.daasuu.bl.ArrowDirection
import com.daasuu.bl.BubblePopupHelper
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseGroupFragment:Fragment() {
    private lateinit var binding: FragmentChooseGroupBinding
    private val args by navArgs<ChooseGroupFragmentArgs>()
    private val viewModel by viewModels<ChooseGroupViewModel>()
    val nameList =
        mutableListOf(
            ChooseGroupUIModel(
                "1",
                "Kaito",
                false
            ),
            ChooseGroupUIModel(
                "2",
                "FateEZ",
                false
            ),
            ChooseGroupUIModel(
                "3",
                "FairyTail",
                false
            ),
            ChooseGroupUIModel(
                "4",
                "Leaf",
                false
            )
        )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentChooseGroupBinding.inflate(inflater).also {
            binding = it
        }.root
    }
    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.set_up_stock)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible =false
    }
//     object CustomSelection: SelectionTracker.SelectionPredicate<K>() {
//        override fun canSetStateForKey(key: K, nextState: Boolean):
//                Boolean {
//            return true
//        }
//
//        override fun canSetStateAtPosition(position: Int, nextState:
//        Boolean): Boolean {
//            return true
//        }
//
//        override fun canSelectMultiple(): Boolean {
//            return false
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        binding.tvFirstCat.text=args.firstCat.name
        binding.tvSecondCat.text=args.secondCat.name
        viewModel.setImageList(nameList)
        setupChipView()
        binding.cbImageView.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked){
                setupRecyclerImage()
            }else{
                setupChipView()
            }
        }

//
//        val tracker = SelectionTracker.Builder(
//            "mySelection",
//            binding.rvImages,
//            MyItemKeysProvider(binding.rvImages),
//            ItemsDetailsLookup(binding.rvImages),
//            StorageStrategy.createLongStorage()
//        ).withSelectionPredicate(
//            SelectionPredicates.createSelectSingleAnything()
//        ).build()
//        adapter.tracker=tracker
//





    }

    fun setupRecyclerImage(){
        binding.rvImages.isVisible = true
        binding.chipGroupChooseGp.isVisible = false
        val adapter = ImageRecyclerAdapter({
            viewModel.selectImage(it)
        },{

        },{
            findNavController().navigate(ChooseGroupFragmentDirections.actionChooseGroupFragmentToEditGroupFragment())
        })
        binding.rvImages.adapter = adapter
        viewModel.groupImages.observe(viewLifecycleOwner){
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }
    }

    fun setupChipView(){
        binding.rvImages.isVisible = false
        binding.chipGroupChooseGp.isVisible = true
        for (name in nameList) {
            val chip = requireContext().createChip(name.name)
            val bubble = BubbleCardBinding.inflate(layoutInflater).root
            val editView = bubble.findViewById<ImageView>(R.id.iv_edit)

            val popupWindow: PopupWindow = BubblePopupHelper.create(requireContext(), bubble)
            popupWindow.width = 300
            popupWindow.height = 200
            chip.setOnClickListener {
                val location = IntArray(2)
                chip.getLocationInWindow(location)
                bubble.arrowDirection = ArrowDirection.BOTTOM
                popupWindow.showAtLocation(chip, Gravity.NO_GRAVITY,location[0], location[1]-chip.height);
            }
            editView.setOnClickListener {
                popupWindow.dismiss()
                findNavController().navigate(ChooseGroupFragmentDirections.actionChooseGroupFragmentToEditGroupFragment())
            }
//            val chip = ItemImageSelectionBinding.inflate(layoutInflater).root
            binding.chipGroupChooseGp.addView(chip)
        }


        binding.chipGroupChooseGp.setOnCheckedStateChangeListener { group, checkedIds ->
            for (index in 0 until group.childCount){
                val chip = group[index] as Chip
                if (chip.isChecked){
                    binding.tvThirdCat.isVisible=true
                    binding.tvThirdCat.setTextColor(requireContext().getColor(R.color.primary_color))
                    binding.tvThirdCat.text = chip.text
                    binding.ivThirdCat.isVisible = true
                }
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}

data class CustomChip(
    val id:String,
    val name:String
)