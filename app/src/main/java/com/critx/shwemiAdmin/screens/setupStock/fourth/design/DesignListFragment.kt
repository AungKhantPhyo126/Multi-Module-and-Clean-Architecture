package com.critx.shwemiAdmin.screens.setupStock.fourth.design

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.critx.common.ui.createChip
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.BubbleCardBinding
import com.critx.shwemiAdmin.databinding.FragmentDesignListBinding
import com.critx.shwemiAdmin.screens.setupStock.fourth.edit.AddCategoryViewModel
import com.critx.shwemiAdmin.uiModel.setupStock.DesignUiModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryCategoryUiModel
import com.daasuu.bl.ArrowDirection
import com.daasuu.bl.BubblePopupHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DesignListFragment:Fragment() {
    private lateinit var binding:FragmentDesignListBinding
    private val viewModel by activityViewModels<AddCategoryViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentDesignListBinding.inflate(inflater).also {
            binding=it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDesign()
        loadingDialog = requireContext().getAlertDialog()

        binding.chipGroupDesign.setOnCheckedStateChangeListener { group, checkedIds ->
            viewModel.selectedDesignIds = checkedIds
        }

        binding.btnConfirm.setOnClickListener {
            findNavController().popBackStack()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                //createGroup
                launch {
                    viewModel.getDesign.collectLatest {
                        if (it.loading) {
                            loadingDialog.show()
                        } else loadingDialog.dismiss()
                        if (!it.success.isNullOrEmpty()) {
                            setUpChipView(it.success.orEmpty())
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
    }

    fun setUpChipView(list:List<DesignUiModel>){
        binding.chipGroupDesign.removeAllViews()
        for (item in list.toSet()) {
            val chip = requireContext().createChip(item.name)
            chip.id = item.id.toInt()
            binding.chipGroupDesign.addView(chip)

        }
        if (!viewModel.selectedDesignIds.isNullOrEmpty()){
            viewModel.selectedDesignIds!!.forEach {
                binding.chipGroupDesign.check(it)
            }
        }
    }

}