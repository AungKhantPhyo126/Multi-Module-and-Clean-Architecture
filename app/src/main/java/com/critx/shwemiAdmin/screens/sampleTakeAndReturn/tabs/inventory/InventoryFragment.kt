package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.inventory

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
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
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.ORDER_STOCK_STATE
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
    private lateinit var barlauncerForVoucher: Any
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
        val newSampleRecyclerAdapter = NewSampleRecyclerAdapter({
            viewModel.removeSample(it)
        }, viewModel)
        val sampleReturnRecyclerAdapter = SampleReturnRecyclerAdapter {
            viewModel.removeSampleForReturn(it)
        }
        binding.layoutSampleReturn.rvSampleReturnInventory.adapter = sampleReturnRecyclerAdapter
        binding.layoutSampleLists.rvSample.adapter = newSampleRecyclerAdapter
        barlauncer = this.getBarLauncherTest(requireContext()) {
            binding.edtScanHere.setText(it)
            viewModel.scanStock(it)
        }
        barlauncerForVoucher = this.getBarLauncherTest(requireContext()) {
            binding.edtScanHere.setText(it)
            viewModel.scanVoucher(it)
        }
        binding.tilScanHere.setEndIconOnClickListener {
            this.scanQrCode(requireContext(), barlauncer)
        }
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedButtonId ->
            if (checkedButtonId == binding.radioButtonSampleTake.id) {
                binding.layoutSampleLists.root.isVisible = true
                binding.layoutSampleReturn.root.isVisible = false
                binding.layoutBtnReturnSample.root.isVisible = false
//                viewModel.resetSample()
            } else if (checkedButtonId == binding.radioButtonSampleReturn.id) {
//                viewModel.getInventorySampleList()
//                viewModel.resetSample()
                binding.layoutSampleLists.root.isVisible = false
                binding.layoutSampleReturn.root.isVisible = true
                binding.layoutBtnReturnSample.root.isVisible = true
            }
        }

        binding.switch1.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.tilScanHere.setEndIconOnClickListener {
                    this.scanQrCode(requireContext(), barlauncerForVoucher)
                }
                binding.edtScanHere.setOnKeyListener(object : View.OnKeyListener {
                    override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                        // If the event is a key-down event on the "enter" button
                        if (event.action == KeyEvent.ACTION_DOWN &&
                            keyCode == KeyEvent.KEYCODE_ENTER
                        ) {
                            // Perform action on key press
                            viewModel.scanVoucher(binding.edtScanHere.text.toString())
                            hideKeyboard(activity, binding.edtScanHere)
                            return true
                        }
                        return false
                    }
                })
            } else {
                binding.tilScanHere.setEndIconOnClickListener {
                    this.scanQrCode(requireContext(), barlauncer)
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
            }
        }

        /**sampleReturn**/
        binding.layoutBtnReturnSample.btnSampleReturn.setOnClickListener {
            viewModel.returnSample(sampleReturnRecyclerAdapter.currentList.map { it.sampleId!! })
        }
        viewModel.returnSampleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data!!) {
                        viewModel.resetSampleForReturn()
                    }
                    viewModel.resetReturnSampleLiveData()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }
//        val sampleReturnRecyclerAdapter = SampleReturnInventoryRecyclerAdapter()
//        binding.layoutSampleReturn.rvSampleReturnInventory.adapter = sampleReturnRecyclerAdapter
//
//            viewModel.getInventorySampleLiveData.observe(viewLifecycleOwner) {
//                when (it) {
//                    is Resource.Loading -> {
//                        loadingDialog.show()
//                    }
//                    is Resource.Success -> {
//                       loadingDialog.dismiss()
//                        sampleReturnRecyclerAdapter.submitList(it.data)
//                    }
//                    is Resource.Error -> {
//                        loadingDialog.dismiss()
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
//
//                    }
//                }
//            }

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

//        binding.layoutBtnGroup.btnAddToHandedList.setOnClickListener {
//            viewModel.addToHandedList()
//        }

//        binding.layoutBtnGroup.btnAddToHandedList.isEnabled =
//            (sharedViewModel.sampleTakeScreenUIState != GIVE_GOLD_STATE ||
//                    sharedViewModel.sampleTakeScreenUIState != ORDER_STOCK_STATE) &&
//                    newSampleRecyclerAdapter.currentList.isEmpty().not()


        viewModel.scanProductCodeLive.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    viewModel.checkSampleUseCase(it.data!!.id)
                    viewModel.resetScanProductCodeLive()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }

        viewModel.voucherScanLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    viewModel.checkSampleWithVoucher(it.data!!.id)
                    viewModel.resetVoucherScanLive()
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
                    viewModel.resetsaveSampleLiveData()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    viewModel.resetsaveSampleLiveData()

                }
            }
        }

        viewModel.sampleLiveDataFromVoucher.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    it.data!!.forEach { sampleItem ->
                        if (binding.radioButtonSampleTake.isChecked) {
                            if (viewModel.scannedSamples.contains(sampleItem)) {
                                Toast.makeText(requireContext(), "Stock Already Scanned", Toast.LENGTH_LONG)
                                    .show()
                            }else{
                                viewModel.addStockSample(sampleItem)
                            }
                        }else if (binding.radioButtonSampleReturn.isChecked){
                            if (viewModel.scannedSamplesForReturn.contains(sampleItem)) {
                                Toast.makeText(requireContext(), "Stock Already Scanned", Toast.LENGTH_LONG)
                                    .show()
                            }else{
                                if (sampleItem.specification.isNullOrEmpty().not()) {
                                    viewModel.addStockSampleForReturn(sampleItem)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "This Stock is not saved as sample",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            }
                        }
                    }
                    viewModel.resetSampleLiveDataFromVoucher()

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
                    if (binding.radioButtonSampleTake.isChecked) {
                        if (viewModel.scannedSamples.contains(it.data!!)) {
                            Toast.makeText(requireContext(), "Stock Already Scanned", Toast.LENGTH_LONG)
                                .show()
                        }else{
                            viewModel.addStockSample(it.data!!)
                        }
                    }else if (binding.radioButtonSampleReturn.isChecked){
                        if (viewModel.scannedSamplesForReturn.contains(it.data!!)) {
                            Toast.makeText(requireContext(), "Stock Already Scanned", Toast.LENGTH_LONG)
                                .show()
                        }else{
                            if (it.data!!.specification.isNullOrEmpty().not()) {
                                viewModel.addStockSampleForReturn(it.data!!)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "This Stock is not saved as sample",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                    }
                    viewModel.resetSampleLiveData()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

                }
            }
        }
        binding.layoutBtnGroup.btnClear.setOnClickListener {
            viewModel.resetSample()
        }
        viewModel.scannedSampleLiveData.observe(viewLifecycleOwner) {
//            binding.layoutBtnGroup.btnAddToHandedList.isEnabled = it.isNotEmpty()
            if (binding.radioButtonSampleTake.isChecked) {
                newSampleRecyclerAdapter.submitList(it)
                newSampleRecyclerAdapter.notifyDataSetChanged()
                binding.layoutBtnGroup.btnSave.isEnabled =
                    it.filter { it.specification.isNullOrEmpty() }.isNotEmpty()

            }

        }
        viewModel.scannedSampleForReturnLiveData.observe(viewLifecycleOwner) {
//            binding.layoutBtnGroup.btnAddToHandedList.isEnabled = it.isNotEmpty()
            if (binding.radioButtonSampleReturn.isChecked) {
                sampleReturnRecyclerAdapter.submitList(it)
                sampleReturnRecyclerAdapter.notifyDataSetChanged()
            }
        }
    }
}