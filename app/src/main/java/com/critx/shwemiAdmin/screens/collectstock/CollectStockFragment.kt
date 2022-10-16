package com.critx.shwemiAdmin.screens.collectstock

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.critx.shwemiAdmin.hideKeyboard
import com.critx.shwemiAdmin.uiModel.collectStock.CollectStockBatchUIModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectStockFragment : Fragment() {
    private lateinit var binding: FragmentCollectStockBinding
    private val viewModel by viewModels<CollectStockViewModel>()
    private lateinit var barlauncer: Any
    private lateinit var loadingDialog: AlertDialog
    private lateinit var adapter:CollectStockRecyclerAdapter

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
        viewModel.stockCodeList = mutableListOf()
        loadingDialog = requireContext().getAlertDialog()
        barlauncer = this.getBarLauncherTest(requireContext()) { viewModel.scanStock(it) }
        binding.tilScanHere.setEndIconOnClickListener {
            scanQrCode(requireContext(), barlauncer)
        }
        adapter = CollectStockRecyclerAdapter {
            viewModel.removeStockCode(it)
        }
        observeForBatch()
        binding.layoutCollectStockBatch.rvCollectStockBatch.adapter = adapter
        binding.edtScanHere.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER) {
                    // Perform action on key press
                    viewModel.scanStock(binding.edtScanHere.text.toString())
                    hideKeyboard(activity,binding.edtScanHere)
                    return true
                }
                return false
            }
        })


        binding.layoutCollectStockBatch.btnNext.setOnClickListener {
            viewModel.resetScannedStockCodeBatch()
            findNavController().navigate(CollectStockFragmentDirections.actionCollectStockFragmentToFillInfoCollectStockFragment(jewelleryType,
                viewModel.scannedStockcodebatch.value?.map {
                    it.productId
                }?.toTypedArray() ?: emptyArray()
            ))
        }


        binding.btnConfirm.setOnClickListener {
            viewModel.getProductId(binding.edtScanHere.text.toString())
        }


        binding.chiptGp.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.contains(R.id.chip_single)) {
                binding.singleSelectedLayout.isVisible = true
                binding.layoutCollectStockBatch.root.isVisible = false
                binding.edtScanHere.text?.clear()
                observeForSingle()

            } else if (checkedIds.contains(R.id.chip_batch)) {
                binding.singleSelectedLayout.isVisible = false
                binding.layoutCollectStockBatch.root.isVisible = true
                binding.edtScanHere.text?.clear()
                observeForBatch()
            }
        }
    }
    fun observeForBatch(){
        viewModel.scannedStockcodebatch.observe(viewLifecycleOwner) {
            binding.layoutCollectStockBatch.btnNext.isEnabled = it.size>0
            val typeList = it.map { it.productType }
            if (it.size > 1){
                jewelleryType = if (typeList.toSet().size ==1){
                    typeList[0]
                }else{
                    null
                }
            }else if(it.size>0){
                jewelleryType = typeList[0]
            }
            binding.layoutCollectStockBatch.tvTotalScannedStockCount.text = typeList.size.toString()
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
            binding.edtScanHere.text?.clear()
        }

        viewModel.scanProductCodeLive.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    //id list
                    val resultItem = CollectStockBatchUIModel(
                        it.data!!.id,
                        binding.edtScanHere.text.toString(),
                        it.data!!.type.toString()
                    )
                    if (viewModel.stockCodeList.contains(resultItem).not()){
                        viewModel.addStockCode(resultItem)
                    }else{
                        Toast.makeText(requireContext(),"Stock Already Scanned",Toast.LENGTH_LONG).show()
                    }
                    viewModel.resetScanProductCodeLive()
//                    findNavController().navigate(CollectStockFragmentDirections.actionCollectStockFragmentToFillInfoCollectStockFragment())
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                }
            }
        }
    }
    fun observeForSingle(){

        viewModel.collectStockSingleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    requireContext().showSuccessDialog(it.data.orEmpty()) {
                        binding.edtScanHere.text?.clear()
                        binding.edtWeight.text?.clear()
                        viewModel.resetCollectStockSingleLiveData()
                    }
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.getProductIdLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    viewModel.collectStock(it.data.orEmpty(), binding.edtWeight.text.toString())
                    viewModel.resetGetProductIdLiveData()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                }
            }
        }
    }
}