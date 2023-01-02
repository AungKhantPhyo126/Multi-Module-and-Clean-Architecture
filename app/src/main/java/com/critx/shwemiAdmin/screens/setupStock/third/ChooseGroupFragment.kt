package com.critx.shwemiAdmin.screens.setupStock.third

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
import androidx.lifecycle.asLiveData
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
import com.critx.shwemiAdmin.screens.setupStock.third.edit.CREATEED_GROUP_ID
import com.critx.shwemiAdmin.uiModel.setupStock.ChooseGroupUIModel
import com.daasuu.bl.ArrowDirection
import com.daasuu.bl.BubblePopupHelper
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChooseGroupFragment : Fragment() {
    private lateinit var binding: FragmentChooseGroupBinding
    private val args by navArgs<ChooseGroupFragmentArgs>()
    private val viewModel by activityViewModels<ChooseGroupViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    private lateinit var adapter: ImageRecyclerAdapter
    private var frequentUse = 0
    var selectedGroupModel: ChooseGroupUIModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
////            viewModel.setSelectGroup(null)
//            findNavController().popBackStack()
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentChooseGroupBinding.inflate(inflater).also {
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

        frequentUse = if (binding.cbFrequentlyUsed.isChecked) 1 else 0
        viewModel.getJewelleryGroup(
            frequentUse,
            args.firstCat.id.toInt(),
            args.secondCat.id.toInt()
        )
        binding.cbFrequentlyUsed.setOnCheckedChangeListener { compoundButton, ischecked ->
            frequentUse = if (ischecked) 1 else 0
            viewModel.getJewelleryGroup(
                frequentUse,
                args.firstCat.id.toInt(),
                args.secondCat.id.toInt()
            )
            adapter.notifyDataSetChanged()
        }
        loadingDialog = requireContext().getAlertDialog()
        binding.tvFirstCat.text = args.firstCat.name
        binding.tvSecondCat.text = args.secondCat.name
        setupRecyclerImage()
//        viewModel.selectedChooseGroupUIModel.observe(viewLifecycleOwner) {
//            if (it != null) {
//                binding.tvThirdCat.isVisible = true
//                binding.tvThirdCat.setTextColor(requireContext().getColor(R.color.primary_color))
//                binding.tvThirdCat.text = it.name
//                binding.chipGroupChooseGp.check(it.id.toInt())
//                viewModel.selectImage(it.id)
////                collectDataForRecyclerView()
//            } else {
//                binding.tvThirdCat.isVisible = false
//            }
//            binding.btnNext.isEnabled = it != null
//        }
        binding.btnNext.setOnClickListener {
            findNavController().navigate(
                ChooseGroupFragmentDirections.actionChooseGroupFragmentToChooseCategoryFragment(
                    args.firstCat,
                    args.secondCat,
                    selectedGroupModel!!
                )
            )
        }


        viewModel.getGroupLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    adapter.submitList(it.data)
                    adapter.notifyDataSetChanged()

                    setupChipView(it.data?.filterNotNull().orEmpty())
                    val selectedItem = it.data!!.filterNotNull().find { it.isChecked }
                    binding.btnNext.isEnabled = selectedItem != null

                    if (!it.data.isNullOrEmpty() && selectedItem != null) {
                        selectedGroupModel = selectedItem
                        binding.chipGroupChooseGp.check(selectedItem.id.toInt())
                        selectedItem.name.let {
                            binding.tvThirdCat.isVisible = true
                            binding.tvThirdCat.setTextColor(requireContext().getColor(R.color.primary_color))
                            binding.tvThirdCat.text = it
                        }
                    } else {
                        binding.tvThirdCat.isVisible = false
                        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ChooseGroupUIModel>(
                            CREATEED_GROUP_ID
                        )
                            ?.observe(viewLifecycleOwner) {

                                viewModel.selectImage(it.id)

                            }
                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    snackBar?.dismiss()
                    snackBar = Snackbar.make(
                        binding.root,
                        it.message!!,
                        Snackbar.LENGTH_LONG
                    )
                    snackBar?.show()
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

    fun navigateWithImageHoverClick(item: ChooseGroupUIModel) {
        findNavController().navigate(
            ChooseGroupFragmentDirections.actionChooseGroupFragmentToEditGroupFragment(
                args.firstCat,
                args.secondCat,
                item
            )
        )
    }

    fun navigateWithEditView(selectedItem:ChooseGroupUIModel) {
        findNavController().navigate(
            ChooseGroupFragmentDirections.actionChooseGroupFragmentToEditGroupFragment(
                args.firstCat,
                args.secondCat,
                selectedItem
            )
        )
    }

    fun navigateWithAddView() {
//        viewModel.setSelectGroup(null)
        findNavController().navigate(
            ChooseGroupFragmentDirections.actionChooseGroupFragmentToEditGroupFragment(
                args.firstCat,
                args.secondCat,
                null
            )
        )
    }

    fun setupRecyclerImage() {

        adapter = ImageRecyclerAdapter({
            //deleteClick
            requireContext().showDeleteSuccessDialog(
                "All items related to this Group will be deleted"
            ) {
                viewModel.deleteJewelleryGroup(it)
            }
        },
            {
                viewModel.selectImage(it.id)
            }, {
                //addNewClick
                navigateWithAddView()
            }, {
                //navigateToEditClick
                navigateWithEditView(it)

            })
        binding.rvImages.adapter = adapter

    }

    fun setupChipView(list: List<ChooseGroupUIModel>) {
        binding.chipGroupChooseGp.removeAllViews()
        val addChipView = requireContext().createChip("Add New")
        addChipView.chipIcon = requireContext().getDrawable(R.drawable.ic_plus)
        addChipView.isCheckable = false
        addChipView.isChipIconVisible = true
        addChipView.setTextColor(requireContext().getColorStateList(R.color.primary_color))
        addChipView.chipIconTint = requireContext().getColorStateList(R.color.primary_color)
        binding.chipGroupChooseGp.addView(addChipView)

        for (item in list.toSet()) {
            val chip = requireContext().createChip(item.name)
            val bubble = BubbleCardBinding.inflate(layoutInflater).root
            val editView = bubble.findViewById<ImageView>(R.id.iv_edit)
//            val deleteView = bubble.findViewById<ImageView>(R.id.iv_trash)

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
            chip.isChecked = item.isChecked
            editView.setOnClickListener {
                popupWindow.dismiss()
                navigateWithEditView(item)
            }

//            deleteView.setOnClickListener {
//                popupWindow.dismiss()
//                requireContext().showDeleteSuccessDialog(
//                    "All items related to this Group will be deleted"
//                ) {
//                    viewModel.deleteJewelleryGroup(chip.id.toString())
//                }
//            }

//            val chip = ItemImageSelectionBinding.inflate(layoutInflater).root
            binding.chipGroupChooseGp.addView(chip)
        }


//        if (viewModel.selectedChooseGroupUIModel.value != null) {
//            binding.chipGroupChooseGp.check(viewModel.selectedChooseGroupUIModel.value!!.id.toInt())
//        } else {
//            binding.tvThirdCat.isVisible = false
//        }

        addChipView.setOnClickListener {
            navigateWithAddView()
        }


        binding.chipGroupChooseGp.setOnCheckedStateChangeListener { group, checkedIds ->
            binding.chipGroupChooseGp.children.toList().find {
                (it as Chip).isChecked
            }.let {
                if (it != null) {
                    val chip = it as Chip
                    viewModel.selectImage(chip.id.toString())
                }
            }

            binding.btnBack.setOnClickListener {
//            viewModel.setSelectGroup(null)
                findNavController().popBackStack()
            }
        }

        fun refreshData() {
            viewModel.getJewelleryGroup(
                frequentUse,
                args.firstCat.id.toInt(),
                args.secondCat.id.toInt()
            )
        }

//    fun collectDataForRecyclerView() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//
//                //getJewelleryGroup
//                launch {
//                    viewModel.getGroupState.collect { uiState ->
//                        if (uiState.loading) {
//                            loadingDialog.show()
//                        } else loadingDialog.dismiss()
//                        if (uiState.successLoading != null) {
//                            adapter.submitList(uiState.successLoading)
//                            adapter.notifyDataSetChanged()
////                            setupChipView(uiState.successLoading.orEmpty())
//                            uiState.successLoading?.find { uiModel ->
//                                uiModel.isChecked
//                            }?.let { checkedModel ->
//                                if (checkedModel != null) {
//                                    binding.tvThirdCat.isVisible = true
//                                    binding.tvThirdCat.setTextColor(requireContext().getColor(R.color.primary_color))
//                                    binding.tvThirdCat.text = checkedModel.name
//                                } else {
//                                    binding.tvThirdCat.isVisible = false
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
    }

}
