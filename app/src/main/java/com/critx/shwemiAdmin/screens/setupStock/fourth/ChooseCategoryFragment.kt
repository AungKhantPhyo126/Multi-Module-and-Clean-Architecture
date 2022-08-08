package com.critx.shwemiAdmin.screens.setupStock.third

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.ui.createChip
import com.critx.common.ui.getAlertDialog
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.*
import com.critx.shwemiAdmin.screens.setupStock.fourth.JewelleryCategoryRecyclerAdapter
import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryCategoryUiModel
import com.daasuu.bl.ArrowDirection
import com.daasuu.bl.BubblePopupHelper
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChooseCategoryFragment : Fragment() {
    private lateinit var binding: FragmentChooseCategoryBinding
    private val args by navArgs<ChooseCategoryFragmentArgs>()
    private val viewModel by viewModels<ChooseCategoryViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    private lateinit var adapter: JewelleryCategoryRecyclerAdapter



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentChooseCategoryBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    private fun toolbarsetup() {

        val toolbarCenterImage: ImageView =
            activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView =
            activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible = true
        toolbarCenterText.text = getString(R.string.set_up_stock)
        toolbarCenterImage.isVisible = false
        toolbarEndIcon.isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        var frequentUse = if (binding.cbFrequentlyUsed.isChecked) 1 else 0
        viewModel.getJewelleryCategory(
            frequentUse,
            args.firstCat.id.toInt(),
            args.secondCat.id.toInt(),
            args.thirdCat.id.toInt()
        )
        binding.cbFrequentlyUsed.setOnCheckedChangeListener { compoundButton, ischecked ->
            frequentUse = if (ischecked) 1 else 0
            viewModel.getJewelleryCategory(
                frequentUse,
                args.firstCat.id.toInt(),
                args.secondCat.id.toInt(),
                args.thirdCat.id.toInt()
            )
        }
        loadingDialog = requireContext().getAlertDialog()
        binding.tvFirstCat.text = args.firstCat.name
        binding.tvSecondCat.text = args.secondCat.name
        binding.tvThirdCat.text = args.thirdCat.name
        setupRecyclerImage()
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                //getJewelleryGroup
                launch {
                    viewModel.getJewelleryCategory.collect {
                        if (it.loading) {
                            loadingDialog.show()
                        } else loadingDialog.dismiss()
                        if (it.successLoading != null) {
                            adapter.submitList(it.successLoading)
                            adapter.notifyDataSetChanged()
                            setupChipView(it.successLoading.orEmpty())
                        }
                    }
                }

                launch {
                    viewModel.event.collectLatest { event ->
                        when (event) {
                            is UiEvent.ShowErrorSnackBar -> {
                                snackBar?.dismiss()
                                snackBar = Snackbar.make(
                                    binding.root,
                                    event.message,
                                    Snackbar.LENGTH_LONG
                                )
                                snackBar?.show()
                            }
                        }
                    }
                }
            }
        }

        binding.cbImageView.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.rvImages.visibility = View.VISIBLE
                binding.chipGroupChooseGp.visibility = View.INVISIBLE
            } else {
                binding.rvImages.visibility = View.INVISIBLE
                binding.chipGroupChooseGp.visibility = View.VISIBLE
            }
        }
    }

    fun navigateWithImageHoverClick(item:ChooseGroupUIModel){
        findNavController().navigate(
            ChooseGroupFragmentDirections.actionChooseGroupFragmentToEditGroupFragment(
                args.firstCat,
                args.secondCat,
                item
            )
        )
    }

    fun navigateWithEditView() {
        findNavController().navigate(ChooseCategoryFragmentDirections.actionChooseCategoryFragmentToAddCategoryFragment(
            args.firstCat,
            args.secondCat,
            args.thirdCat
        ))
    }

    fun navigateWithAddView() {
        findNavController().navigate(ChooseCategoryFragmentDirections.actionChooseCategoryFragmentToAddCategoryFragment(
            args.firstCat,
            args.secondCat,
            args.thirdCat
        ))
    }

    fun setupRecyclerImage() {
        adapter = JewelleryCategoryRecyclerAdapter({
            viewModel.selectImage(it)
            viewLifecycleOwner.lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                    //getJewelleryGroup
                    launch {
                        viewModel.getJewelleryCategory.collect {uiState->
                            if (uiState.loading) {
                                loadingDialog.show()
                            } else loadingDialog.dismiss()
                            if (uiState.successLoading != null) {
                                adapter.submitList(uiState.successLoading)
                                adapter.notifyDataSetChanged()
                                setupChipView(uiState.successLoading.orEmpty())
                                uiState.successLoading?.find { uiModel->
                                    uiModel.isChecked
                                }?.name.let {checkedName->
                                    if (checkedName != null){
                                        binding.tvFourthCat.isVisible = true
                                        binding.tvFourthCat.setTextColor(requireContext().getColor(R.color.primary_color))
                                        binding.tvFourthCat.text = checkedName
                                    }else{
                                        binding.tvFourthCat.isVisible=false
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },{
            navigateWithAddView()
        },{
            navigateWithEditView()
        })
        binding.rvImages.adapter = adapter
    }

    fun setupChipView(list: List<JewelleryCategoryUiModel>) {
        binding.chipGroupChooseGp.removeAllViews()
        for (item in list.toSet()) {
            val chip = requireContext().createChip(item.name)
            val bubble = BubbleCardBinding.inflate(layoutInflater).root
            val editView = bubble.findViewById<ImageView>(R.id.iv_edit)

            val popupWindow: PopupWindow = BubblePopupHelper.create(requireContext(), bubble)
            popupWindow.width = 300
            popupWindow.height = 200
            chip.id = item.id.toInt()
            chip.setOnLongClickListener {
                val location = IntArray(2)
                chip.getLocationInWindow(location)
                bubble.arrowDirection = ArrowDirection.BOTTOM
                popupWindow.showAtLocation(
                    chip,
                    Gravity.NO_GRAVITY,
                    location[0],
                    location[1] - chip.height
                )
                return@setOnLongClickListener true
            }
            editView.setOnClickListener {
                popupWindow.dismiss()
                viewModel.selectedJewelleryCategory = JewelleryCategoryUiModel(
                    chip.id.toString(),item.name,item.imageUrl,chip.isChecked,
                    item.isFrequentlyUse
                )
//                navigateWithEditView()
            }
//            val chip = ItemImageSelectionBinding.inflate(layoutInflater).root
            binding.chipGroupChooseGp.addView(chip)
        }
        val addChipView = requireContext().createChip("Add New")
        addChipView.chipIcon = requireContext().getDrawable(R.drawable.ic_plus)
        addChipView.isCheckable = false
        addChipView.isChipIconVisible = true
        addChipView.setTextColor(requireContext().getColorStateList(R.color.primary_color))
        addChipView.chipIconTint = requireContext().getColorStateList(R.color.primary_color)
        binding.chipGroupChooseGp.addView(addChipView)


        addChipView.setOnClickListener {
            navigateWithEditView()

        }

        binding.chipGroupChooseGp.setOnCheckedStateChangeListener { group, checkedIds ->
            binding.chipGroupChooseGp.children.toList().find {
                (it as Chip).isChecked
            }.let {
                if (it != null) {
                    val chip = it as Chip
                    viewModel.selectedJewelleryCategory = JewelleryCategoryUiModel(
                        chip.id.toString(),chip.text.toString(),
                        list.find { it.id == chip.id.toString() }?.imageUrl?:"",
                        chip.isChecked,
                        list.find { it.id == chip.id.toString() }?.isFrequentlyUse?:false
                    )
                    binding.tvFourthCat.isVisible = true
                    binding.tvFourthCat.setTextColor(requireContext().getColor(R.color.primary_color))
                    binding.tvFourthCat.text = chip.text
                } else {
                    viewModel.selectedJewelleryCategory = null
                    binding.tvFourthCat.isVisible = false
                }

            }

        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}
