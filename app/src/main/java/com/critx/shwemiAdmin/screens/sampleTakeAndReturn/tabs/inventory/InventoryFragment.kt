package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.inventory

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.critx.common.qrscan.getBarLauncher
import com.critx.common.qrscan.getBarLauncherTest
import com.critx.common.qrscan.scanQrCode
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentInventoryBinding
import com.critx.shwemiAdmin.hideKeyboard
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.GIVE_GOLD_STATE
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.SAMPLE_TAKE_STATE
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.SampleTakeAndReturnViewModel
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InventoryFragment : Fragment() {
    private lateinit var binding: FragmentInventoryBinding
    private lateinit var barlauncer: Any
    private val viewModel by viewModels<InventoryViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var loadingDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return FragmentInventoryBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = requireContext().getAlertDialog()

        barlauncer = this.getBarLauncherTest(requireContext()) {
            binding.edtScanHere.setText(it)
            viewModel.scanStock(it)
        }
        binding.tilScanHere.setEndIconOnClickListener {
            this.scanQrCode(requireContext(), barlauncer)
        }


        binding.layoutBtnGroup.btnSave.setOnClickListener {
            viewModel.saveSamples()
        }
        binding.edtScanHere.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    // Perform action on key press
                    viewModel.scanStock(binding.edtScanHere.text.toString())
                    hideKeyboard(activity, binding.edtScanHere)
                    return true
                }
                return false
            }
        })

        binding.layoutBtnGroup.btnAddToHandedList.setOnClickListener {
            viewModel.addToHandedList()
        }

        val newSampleRecyclerAdapter = NewSampleRecyclerAdapter({

        }, viewModel)

        binding.layoutBtnGroup.btnAddToHandedList.isEnabled =
            sharedViewModel.sampleTakeScreenUIState != GIVE_GOLD_STATE


        binding.layoutSampleLists.rvSample.adapter = newSampleRecyclerAdapter
        viewModel.scanProductCodeLive.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    viewModel.checkSampleUseCase(it.data!!.id)
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }



        viewModel.saveSampleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data!!) {

                    }
                    newSampleRecyclerAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }

        viewModel.addToHandedListLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data!!) {
                        viewModel.resetSample()
                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }

        viewModel.sampleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    if (viewModel.scannedSamples.contains(it.data!![0])) {
                        Toast.makeText(requireContext(), "Stock Already Scanned", Toast.LENGTH_LONG)
                            .show()

                    } else {
                        viewModel.addStockSample(it.data!![0])
                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }

        viewModel.scannedSampleLiveData.observe(viewLifecycleOwner) {
            newSampleRecyclerAdapter.submitList(it)
            newSampleRecyclerAdapter.notifyDataSetChanged()
        }

    }
}