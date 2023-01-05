package com.critx.shwemiAdmin.screens.setupStock.first

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.critx.common.ui.createChip
import com.critx.common.ui.getAlertDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.UiEvent
import com.critx.shwemiAdmin.databinding.FragmentSetUpStocklBinding
import com.critx.shwemiAdmin.screens.dailyGoldPrice.DailyGoldPriceFragmentDirections
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import com.critx.shwemiAdmin.uiModel.setupStock.JewelleryTypeUiModel
import com.critx.shwemiAdmin.workerManager.RefreshTokenWorker
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SetupStockFragment : Fragment() {
    private lateinit var binding: FragmentSetUpStocklBinding
    private lateinit var loadingDialog: AlertDialog
    private var snackBar: Snackbar? = null
    private val viewModel by viewModels<SetupStockViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSetUpStocklBinding.inflate(inflater).also {
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
        viewModel.jewelleryTypeState.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading->{
                    loadingDialog.show()
                }
                is Resource.Success->{
                    loadingDialog.dismiss()
                    binding.chipGroupJewelleryType.removeAllViews()
                    for (item in it.data!!) {
                        val chip = requireContext().createChip(item.name)
                        chip.id = item.id.toInt()
                        binding.chipGroupJewelleryType.addView(chip)
                    }
                }
                is Resource.Error->{
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


        var checkedChipId = 0
        binding.chipGroupJewelleryType.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChip = binding.chipGroupJewelleryType.children.toList().find {
                (it as Chip).isChecked
            }
            if (selectedChip!=null){
                    val chip = selectedChip as Chip
                    checkedChipId = chip.id
                    binding.tvFirstCat.setTextColor(requireContext().getColor(R.color.primary_color))
                    binding.tvFirstCat.text = chip.text
                    binding.ivFirst.isVisible = true
            }else{
                binding.tvFirstCat.text = requireContext().getString(R.string.jewellery_type)
                binding.ivFirst.isVisible = false
            }

        }

        binding.btnNext.setOnClickListener {
            if (binding.tvFirstCat.text != requireContext().getString(R.string.jewellery_type)) {
                findNavController().navigate(
                    SetupStockFragmentDirections.actionSetupStockFragmentToChooseJewelleryQualityFragment(
                        JewelleryTypeUiModel(
                            checkedChipId.toString(),
                            binding.tvFirstCat.text.toString()
                        )
                    )
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please choose at least one item",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

}

data class CustomChip(
    val id: String,
    val name: String
)