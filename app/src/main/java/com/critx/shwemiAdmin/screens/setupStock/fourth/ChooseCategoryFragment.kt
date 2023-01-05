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
import com.critx.commonkotlin.util.Resource
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
    private var frequentUse = 0
    var selectedGroupModel: JewelleryCategoryUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
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
        viewModel.getJewelleryCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    adapter.submitList(it.data)
                    adapter.notifyDataSetChanged()
                    setupChipView(it.data.orEmpty())
                    val selectedItem = it.data!!.filterNotNull().find { it.isChecked }
                    binding.btnNext.isEnabled = selectedItem != null

                    if (!it.data.isNullOrEmpty() && selectedItem != null) {
                        selectedGroupModel = selectedItem
                        binding.chipGpCategory.check(selectedItem.id.toInt())
                        selectedItem.name.let {
                            binding.tvFourthCat.isVisible = true
                            binding.ivFourthCat.isVisible = true
                            binding.tvFourthCat.setTextColor(requireContext().getColor(R.color.primary_color))
                            binding.tvFourthCat.text = it
                        }
                    } else {
                        binding.tvFourthCat.isVisible = false
                        binding.ivFourthCat.isVisible = false
                        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<JewelleryCategoryUiModel>(
                            CREATED_CATEGORY_ID
                        )
                            ?.observe(viewLifecycleOwner) {
                                if (
                                    findNavController().previousBackStackEntry?.destination?.id == R.id.editGroupFragment ||
                                    findNavController().previousBackStackEntry?.destination?.id == R.id.chooseCategoryFragment
                                ) {
                                    viewModel.selectImage(it.id)
                                }

                            }
                    }

                }
                is Resource.Error -> {
                    loadingDialog.dismiss()

                }
            }
        }


        binding.cbImageView.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.rvImages.visibility = View.VISIBLE
                binding.chipGpCategory.visibility = View.INVISIBLE
            } else {
                binding.rvImages.visibility = View.INVISIBLE
                binding.chipGpCategory.visibility = View.VISIBLE
            }
        }
    }


    fun navigateWithEditView(selectedItem: JewelleryCategoryUiModel) {
        findNavController().navigate(
            ChooseCategoryFragmentDirections.actionChooseCategoryFragmentToAddCategoryFragment(
                args.firstCat,
                args.secondCat,
                args.thirdCat,
                selectedItem
            )
        )
    }

    fun navigateWithAddView() {
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
            viewModel.selectImage(it.id)
        }, {
            navigateWithAddView()
        }, {
            navigateWithEditView(it)
        }, {
            //deleteclick
            requireContext().showDeleteSuccessDialog(
                "All items related to this Category will be deleted"
            ) {
                viewModel.deleteJewelleryCategory(it)
            }
        },{
            //eye click
            findNavController().navigate(ChooseCategoryFragmentDirections.actionGlobalPhotoViewFragment(it))
        })
        binding.rvImages.adapter = adapter
    }

    fun setupChipView(list: List<JewelleryCategoryUiModel>) {
        binding.chipGpCategory.removeAllViews()
        val addChipView = requireContext().createChip("Add New")
        addChipView.chipIcon = requireContext().getDrawable(R.drawable.ic_plus)
        addChipView.isCheckable = false
        addChipView.isChipIconVisible = true
        addChipView.setTextColor(requireContext().getColorStateList(R.color.primary_color))
        addChipView.chipIconTint = requireContext().getColorStateList(R.color.primary_color)
        binding.chipGpCategory.addView(addChipView)

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
                navigateWithEditView(item)
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
            binding.chipGpCategory.addView(chip)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<JewelleryCategoryUiModel>(
            CREATED_CATEGORY_ID
        )
            ?.observe(viewLifecycleOwner) {
                binding.chipGpCategory.check(it.id.toInt())
            }
        addChipView.setOnClickListener {
            navigateWithAddView()
        }

        binding.chipGpCategory.setOnCheckedStateChangeListener { group, checkedIds ->
            binding.chipGpCategory.children.toList().find {
                (it as Chip).isChecked
            }.let {
                if (it != null) {
                    val chip = it as Chip
                    viewModel.selectImage(chip.id.toString())
                } else {
                    viewModel.deSelectAll()
                }
            }

            binding.btnBack.setOnClickListener {
//            viewModel.setSelectGroup(null)
                findNavController().popBackStack()
            }
        }

        binding.btnBack.setOnClickListener {

            findNavController().popBackStack()
        }
    }


}
