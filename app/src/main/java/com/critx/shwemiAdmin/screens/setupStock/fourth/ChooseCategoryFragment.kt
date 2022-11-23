package com.critx.shwemiAdmin.screens.setupStock.fourth

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.ui.createChip
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showDeleteSuccessDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.*
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.CREATED_CATEGORY_ID

import com.critx.shwemiAdmin.screens.setupStock.third.ChooseCategoryViewModel
import com.critx.shwemiAdmin.screens.setupStock.third.ChooseGroupFragmentDirections
import com.critx.shwemiAdmin.screens.setupStock.third.ChooseGroupViewModel
import com.critx.shwemiAdmin.screens.setupStock.third.edit.CREATEED_GROUP_ID
import com.critx.shwemiAdmin.uiModel.setupStock.AvgKPYUiModel
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
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private var frequentUse = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.setSelectedCategory(null)
            findNavController().popBackStack()
        }
    }

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


    fun refreshData() {
        viewModel.getJewelleryCategory(
            frequentUse,
            args.firstCat.id.toInt(),
            args.secondCat.id.toInt(),
            args.thirdCat.id.toInt()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        frequentUse = if (binding.cbFrequentlyUsed.isChecked) 1 else 0
        viewModel.getJewelleryCategory(
            frequentUse,
            args.firstCat.id.toInt(),
            args.secondCat.id.toInt(),
            args.thirdCat.id.toInt()
        )
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            CREATEED_GROUP_ID,
            args.thirdCat
        )
//        viewModel._selectedJewelleryCategory.observe(viewLifecycleOwner){
//            sharedViewModel.fourthCat = it
//            binding.btnNext.isEnabled = it != null
//        }
        viewModel._selectedJewelleryCategory.observe(viewLifecycleOwner) {
            sharedViewModel.fourthCat = it
            if (it != null) {
                binding.tvFourthCat.isVisible = true
                binding.tvFourthCat.setTextColor(requireContext().getColor(R.color.primary_color))
                binding.tvFourthCat.text = it.name
                binding.chipGroupChooseGp.check(it.id.toInt())
                viewModel.selectImage(it.id)
                collectDataForRecyclerView()
            } else {
                binding.tvFourthCat.isVisible = false
            }
            binding.btnNext.isEnabled = it != null
        }

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

        binding.btnNext.setOnClickListener {
            if (sharedViewModel.thirdCatForRecommendCat != null) {
                sharedViewModel.addRecommendCat(sharedViewModel.fourthCat!!)
                sharedViewModel.resetForRecommendCat()
                findNavController().navigate(
                    ChooseCategoryFragmentDirections.actionChooseCategoryFragmentToRecommendStockFragment(
                        sharedViewModel.fourthCat!!
                    )
                )
            }else{
                sharedViewModel.firstCat = args.firstCat
                sharedViewModel.secondCat = args.secondCat
                sharedViewModel.thirdCat = args.thirdCat
                findNavController().navigate(ChooseCategoryFragmentDirections.actionChooseCategoryFragmentToProductCreateFragment())
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                //deleteCategory
                launch {
                    viewModel.deleteCategoryState.collectLatest {
                        if (it.deleteLoading) {
                            loadingDialog.show()
                        } else loadingDialog.dismiss()
                        if (it.deleteSuccessLoading != null) {
                            viewModel.setSelectedCategory(null)
                            requireContext().showSuccessDialog("Category Deleted") {
                                it.deleteSuccessLoading = null
                                refreshData()
                            }
                        }
                    }
                }

                //getJewelleryGroup
                launch {
                    viewModel.getJewelleryCategory.collect {
                        if (it.loading) {
                            loadingDialog.show()
                        } else loadingDialog.dismiss()
                        if (it.successLoading != null) {
                            if(sharedViewModel.thirdCatForRecommendCat != null){
                                adapter.submitList(it.successLoading!!.filterNotNull())
                                adapter.notifyDataSetChanged()
                            }else{
                                adapter.submitList(it.successLoading)
                                adapter.notifyDataSetChanged()
                            }

                            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<JewelleryCategoryUiModel>(
                                CREATED_CATEGORY_ID
                            )
                                ?.observe(viewLifecycleOwner) {
                                    if (
                                        findNavController().previousBackStackEntry?.destination?.id == R.id.editGroupFragment ||
                                        findNavController().previousBackStackEntry?.destination?.id == R.id.chooseCategoryFragment
                                    ) {
                                        viewModel.setSelectedCategory(it)
                                    }

                                }
                            setupChipView(it.successLoading?.filterNotNull().orEmpty())
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

//    fun navigateWithImageHoverClick(item: ChooseGroupUIModel) {
//        findNavController().navigate(
//            ChooseGroupFragmentDirections.actionChooseGroupFragmentToEditGroupFragment(
//                args.firstCat,
//                args.secondCat,
//                item
//            )
//        )
//    }

    fun navigateWithEditView() {
        findNavController().navigate(
            ChooseCategoryFragmentDirections.actionChooseCategoryFragmentToAddCategoryFragment(
                args.firstCat,
                args.secondCat,
                args.thirdCat,
                sharedViewModel.fourthCat
            )
        )
    }

    fun navigateWithAddView() {
        viewModel.setSelectedCategory(null)
        findNavController().navigate(
            ChooseCategoryFragmentDirections.actionChooseCategoryFragmentToAddCategoryFragment(
                args.firstCat,
                args.secondCat,
                args.thirdCat,
                null
            )
        )
    }

    fun setupRecyclerImage() {
        adapter = JewelleryCategoryRecyclerAdapter({
            viewModel.setSelectedCategory(it)
        }, {
            navigateWithAddView()
        }, {
            viewModel.setSelectedCategory(it)
            navigateWithEditView()
        }, {
            //deleteclick
            requireContext().showDeleteSuccessDialog(
                "All items related to this Category will be deleted"
            ) {
                viewModel.deleteJewelleryCategory(it)
            }
        })
        binding.rvImages.adapter = adapter
    }

    fun setupChipView(list: List<JewelleryCategoryUiModel>) {
        binding.chipGroupChooseGp.removeAllViews()
        val addChipView = requireContext().createChip("Add New")
        addChipView.chipIcon = requireContext().getDrawable(R.drawable.ic_plus)
        addChipView.isCheckable = false
        addChipView.isChipIconVisible = true
        addChipView.setTextColor(requireContext().getColorStateList(R.color.primary_color))
        addChipView.chipIconTint = requireContext().getColorStateList(R.color.primary_color)
        if(sharedViewModel.thirdCatForRecommendCat == null){
            binding.chipGroupChooseGp.addView(addChipView)
        }
        for (item in list.toSet()) {
            val chip = requireContext().createChip(item.name)
            val bubble = BubbleCardBinding.inflate(layoutInflater).root
            val editView = bubble.findViewById<ImageView>(R.id.iv_edit)
//            val deleteView = bubble.findViewById<ImageView>(R.id.iv_trash)

            val popupWindow: PopupWindow = BubblePopupHelper.create(requireContext(), bubble)
            popupWindow.width = 300
            popupWindow.height = 200
            chip.id = item.id.toInt()
            chip.isChecked = item.isChecked
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
                if (!binding.rvImages.isVisible) {
                    viewModel.setSelectedCategory(item)
                }
                navigateWithEditView()
            }

//            deleteView.setOnClickListener {
//                popupWindow.dismiss()
//                requireContext().showDeleteSuccessDialog(
//                    "All items related to this Category will be deleted"
//                ) {
//                    viewModel.deleteJewelleryCategory(item.id)
//                }
//            }
//            val chip = ItemImageSelectionBinding.inflate(layoutInflater).root
            binding.chipGroupChooseGp.addView(chip)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<JewelleryCategoryUiModel>(
            CREATED_CATEGORY_ID
        )
            ?.observe(viewLifecycleOwner) {
                binding.chipGroupChooseGp.check(it.id.toInt())
            }
        addChipView.setOnClickListener {
            navigateWithAddView()
        }

        binding.chipGroupChooseGp.setOnCheckedStateChangeListener { group, checkedIds ->
            binding.chipGroupChooseGp.children.toList().find {
                (it as Chip).isChecked
            }.let {
                if (it != null) {
                    val chip = it as Chip
                    if (!binding.rvImages.isVisible) {
                        viewModel.setSelectedCategory(list.find { it.id == chip.id.toString() })
                    }
                } else {
                    viewModel.setSelectedCategory(null)
                }

            }

        }
        if (viewModel.selectedJewelleryCategory.value != null) {
            binding.chipGroupChooseGp.check(viewModel.selectedJewelleryCategory.value!!.id.toInt())
        } else {
            binding.tvFourthCat.isVisible = false
        }
        binding.btnBack.setOnClickListener {
            viewModel.setSelectedCategory(null)
            findNavController().popBackStack()
        }
    }

    fun collectDataForRecyclerView() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                //getJewelleryCategory
                launch {
                    viewModel.getJewelleryCategory.collect { uiState ->
                        if (uiState.loading) {
                            loadingDialog.show()
                        } else loadingDialog.dismiss()
                        if (uiState.successLoading != null) {
                            adapter.submitList(uiState.successLoading)
                            adapter.notifyDataSetChanged()
//                            setupChipView(uiState.successLoading.orEmpty())
                            uiState.successLoading?.filterNotNull()?.find { uiModel ->
                                uiModel.isChecked
                            }?.name.let { checkedName ->
                                if (checkedName != null) {
                                    binding.tvFourthCat.isVisible = true
                                    binding.tvFourthCat.setTextColor(requireContext().getColor(R.color.primary_color))
                                    binding.tvFourthCat.text = checkedName
                                } else {
                                    binding.tvFourthCat.isVisible = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
