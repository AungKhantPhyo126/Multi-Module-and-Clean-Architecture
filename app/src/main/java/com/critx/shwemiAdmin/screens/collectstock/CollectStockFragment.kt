package com.critx.shwemiAdmin.screens.collectstock

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.critx.common.qrscan.getBarLauncherTest
import com.critx.common.qrscan.scanQrCode
import com.critx.common.ui.getAlertDialog
import com.critx.common.ui.showSuccessDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentCollectStockBinding
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel

class CollectStockFragment : Fragment() {
    private lateinit var binding: FragmentCollectStockBinding
    private val viewModel by viewModels<CollectStockVIewModel>()
    private lateinit var barlauncer: Any
    private lateinit var loadingDialog: AlertDialog

    var jewelleryType:String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentCollectStockBinding.inflate(inflater).also {
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
        toolbarCenterText.text = getString(R.string.collect_stock_and_give_data)
        toolbarCenterImage.isVisible = false
        toolbarEndIcon.isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        loadingDialog = requireContext().getAlertDialog()
        barlauncer = this.getBarLauncherTest(requireContext()) { viewModel.scanStock(it) }
        binding.ivScan.setOnClickListener {
            scanQrCode(requireContext(), barlauncer)
        }
        val adapter = CollectStockRecyclerAdapter {
            viewModel.removeStockCode(it)
        }

        binding.layoutCollectStockBatch.rvCollectStockBatch.adapter = adapter
        viewModel.scannedStockcodebatch.observe(viewLifecycleOwner) {
            binding.layoutCollectStockBatch.btnNext.isEnabled = it.size>0
            if (it.size > 1){
                val typeList = it.map { it.productType }
                jewelleryType = if (typeList.toSet().size ==1){
                    typeList[0]
                }else{
                    null
                }
            }
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }

        binding.edtStockCode.setOnKeyListener { view, keyCode, keyevent ->
            //If the keyevent is a key-down event on the "enter" button
            if (keyevent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                //...
                // Perform your action on key press here
                viewModel.scanStock(binding.edtStockCode.text.toString())
                binding.edtStockCode.text?.clear()
                true
            } else false
        }




        binding.layoutCollectStockBatch.btnNext.setOnClickListener {
            findNavController().navigate(CollectStockFragmentDirections.actionCollectStockFragmentToFillInfoCollectStockFragment(jewelleryType,
                viewModel.scannedStockcodebatch.value?.map {
                    it.productId
                }?.toTypedArray() ?: emptyArray()
            ))
        }

        viewModel.scanProductCodeLive.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    //id list
                    viewModel.addStockCode(
                        CollectStockBatchUIModel(
                            it.data!!.id,
                            binding.edtStockCode.text.toString(),
                            it.data!!.type.toString()
                        )
                    )
//                    findNavController().navigate(CollectStockFragmentDirections.actionCollectStockFragmentToFillInfoCollectStockFragment())
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                }
            }
        }

        viewModel.getProductIdLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    binding.edtStockCode.setText(it.data)
                    viewModel.collectStock(it.data.orEmpty(), binding.edtWeight.text.toString())
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                }
            }
        }

        viewModel.collectStockSingleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data.orEmpty()) {
                        binding.edtStockCode.text?.clear()
                        binding.edtWeight.text?.clear()
                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                }
            }
        }

        binding.btnConfirm.setOnClickListener {
            viewModel.getProductId(binding.edtStockCode.text.toString())
        }

        binding.chiptGp.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.contains(R.id.chip_single)) {
                binding.singleSelectedLayout.isVisible = true
                binding.layoutCollectStockBatch.root.isVisible = false
            } else if (checkedIds.contains(R.id.chip_batch)) {
                binding.singleSelectedLayout.isVisible = false
                binding.layoutCollectStockBatch.root.isVisible = true
            }
        }
    }
}