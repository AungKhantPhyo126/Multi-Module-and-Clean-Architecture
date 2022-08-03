package com.critx.shwemiAdmin.screens.setupStock.second

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.ui.createChip
import com.critx.common.ui.getAlertDialog
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.BubbleCardBinding
import com.critx.shwemiAdmin.databinding.FragmentChooseJewelleryQualityBinding
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryQualityUiModel
import com.daasuu.bl.ArrowDirection
import com.daasuu.bl.BubblePopupHelper
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChooseJewelleryQualityFragment:Fragment() {
    private lateinit var binding: FragmentChooseJewelleryQualityBinding
    private val args by navArgs<ChooseJewelleryQualityFragmentArgs>()
    private val viewModel by viewModels<JewelleryQualityViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentChooseJewelleryQualityBinding.inflate(inflater).also {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        loadingDialog = requireContext().getAlertDialog()

        binding.tvFirstCat.text = args.firstCat.name
//        val nameList =
//            arrayListOf("Extra Soft", "Soft", "Medium", "Hard", "Extra Hard",
//                "Extra Soft", "Soft", "Medium", "Hard", "Extra Hard")
//        val chipIdList = mutableListOf<Int>()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){

                //getJewelleryType
                launch {
                    viewModel.jewelleryQualityState.collectLatest {
                        if (it.loading){
                            loadingDialog.show()
                        }else loadingDialog.dismiss()
                        if (!it.successLoading.isNullOrEmpty()) {
                            for (item in it.successLoading!!) {
                                val chip = requireContext().createChip(item.name)
                                chip.id = item.id.toInt()
                                binding.chipGroupJewelleryQuality.addView(chip)
                            }
                        }
                    }
                }
                //Error Event
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
        var checkedChipId = 0
        binding.chipGroupJewelleryQuality.setOnCheckedStateChangeListener { group, checkedIds ->
            for (index in 0 until group.childCount){
                val chip = group[index] as Chip
                if (chip.isChecked){
                    checkedChipId = chip.id
                    binding.tvSecondCat.isVisible=true
                    binding.tvSecondCat.setTextColor(requireContext().getColor(R.color.primary_color))
                    binding.tvSecondCat.text = chip.text
                    binding.ivSecond.isVisible = true
                }
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnNext.setOnClickListener {
            if (binding.tvSecondCat.isVisible){
                findNavController().navigate(
                    ChooseJewelleryQualityFragmentDirections.actionChooseJewelleryQualityFragmentToChooseGroupFragment(
                    args.firstCat,
                        JewelleryQualityUiModel(checkedChipId.toString(),binding.tvSecondCat.text.toString())
                ))
            }else{
                Toast.makeText(requireContext(),"Please choose at least one category", Toast.LENGTH_LONG).show()
            }

        }

    }
}