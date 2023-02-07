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
import androidx.core.view.children
import androidx.core.view.get
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
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.BubbleCardBinding
import com.critx.shwemiAdmin.databinding.FragmentChooseJewelleryQualityBinding
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryQualityUiModel
import com.daasuu.bl.ArrowDirection
import com.daasuu.bl.BubblePopupHelper
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChooseJewelleryQualityFragment : Fragment() {
    private lateinit var binding: FragmentChooseJewelleryQualityBinding
    private val args by navArgs<ChooseJewelleryQualityFragmentArgs>()
    private val viewModel by viewModels<JewelleryQualityViewModel>()
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    var directNavigateList = mutableListOf<String>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    var checkedChipId = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentChooseJewelleryQualityBinding.inflate(inflater).also {
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
        loadingDialog = requireContext().getAlertDialog()
        viewModel.getJewelleryQuality()

        binding.tvFirstCat.text = args.firstCat.name
        viewModel.jewelleryQualityLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    for (item in it.data!!) {
                        val chip = requireContext().createChip(item.name)
                        chip.id = item.id.toInt()
                        binding.chipGroupJewelleryQuality.addView(chip)
                        if (item.name == "18K" || item.name == "စိန်ထည်" || item.name == "အခေါက်ထည်") {
                            directNavigateList.add(chip.id.toString())
                        }
                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    snackBar?.dismiss()
                    snackBar = Snackbar.make(
                        binding.root,
                        it.message.orEmpty(),
                        Snackbar.LENGTH_LONG
                    )
                    snackBar?.show()
                }
            }
        }

        binding.chipGroupJewelleryQuality.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChip = binding.chipGroupJewelleryQuality.children.toList().find {
                (it as Chip).isChecked
            }
            if (selectedChip!=null){
                val chip = selectedChip as Chip
                checkedChipId = chip.id
                binding.tvSecondCat.setTextColor(requireContext().getColor(R.color.primary_color))
                binding.tvSecondCat.text = chip.text
                binding.ivSecond.isVisible = true
                binding.tvSecondCat.isVisible = true

            }else{
                binding.tvSecondCat.text =""
                binding.tvSecondCat.isVisible = false
                binding.ivSecond.isVisible = false
            }
        }


        binding.btnNext.setOnClickListener {
            if (directNavigateList.contains(binding.chipGroupJewelleryQuality.checkedChipId.toString())) {
                findNavController().navigate(ChooseJewelleryQualityFragmentDirections.actionChooseJewelleryQualityFragmentToProductCreateFragment(args.firstCat.id,checkedChipId.toString(),null,null,null))
            } else if (binding.tvSecondCat.isVisible) {
                findNavController().navigate(
                    com.critx.shwemiAdmin.screens.setupStock.second.ChooseJewelleryQualityFragmentDirections.actionChooseJewelleryQualityFragmentToChooseGroupFragment(
                        args.firstCat,
                        com.critx.shwemiAdmin.uiModel.setupStock.JewelleryQualityUiModel(
                            checkedChipId.toString(),
                            binding.tvSecondCat.text.toString()
                        )
                    )
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please choose at least one category",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }
}