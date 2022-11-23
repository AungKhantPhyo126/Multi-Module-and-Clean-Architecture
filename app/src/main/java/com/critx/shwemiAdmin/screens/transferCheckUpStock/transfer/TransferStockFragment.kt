package com.critx.shwemiAdmin.screens.transferCheckUpStock.transfer

import com.critx.shwemiAdmin.screens.transferCheckUpStock.checkup.StockRecyclerAdapter
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
import com.critx.common.qrscan.getBarLauncher
import com.critx.common.qrscan.getBarLauncherTest
import com.critx.common.qrscan.scanQrCode
import com.critx.common.ui.getAlertDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentCheckUpBinding
import com.critx.shwemiAdmin.databinding.FragmentTransferStockBinding
import com.critx.shwemiAdmin.screens.transferCheckUpStock.TransferCheckUpStockFragmentDirections
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel
import com.critx.shwemiAdmin.uiModel.discount.DiscountUIModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferStockFragment:Fragment() {
    private lateinit var binding:FragmentTransferStockBinding
    private lateinit var barlauncherBox:Any
    private lateinit var barlauncherStock:Any
    private lateinit var loadingDialog: AlertDialog
    private val viewModel by viewModels<TransferStockViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentTransferStockBinding.inflate(inflater).also {
            binding= it
        }.root
    }
    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.transfer_checkup_stock)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible =false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        barlauncherBox = this.getBarLauncherTest(requireContext()) {
            binding.edtScanBox.setText(it)
            viewModel.getBoxData(it)
        }
        barlauncherStock = this.getBarLauncherTest(requireContext()) {
            binding.edtScanStock.setText(it)
            viewModel.scanStock(it)
        }
        loadingDialog = requireContext().getAlertDialog()
        binding.tilScanBox.setEndIconOnClickListener {
            scanQrCode(requireContext(),barlauncherBox)
        }
        binding.tilScanStock.setEndIconOnClickListener {
            scanQrCode(requireContext(),barlauncherStock)
        }
        val adapter = StockRecyclerAdapter{
            viewModel.removeStockCode(it)
        }
        binding.includeScannedStockList.rvStockCodeList.adapter = adapter
        binding.edtScanBox.setOnKeyListener { view, keyCode, keyevent ->
            //If the keyevent is a key-down event on the "enter" button
            if (keyevent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                // Perform your action on key press here
                viewModel.getBoxData(binding.edtScanBox.text.toString())
                true
            } else false
        }

        binding.edtScanStock.setOnKeyListener { view, keyCode, keyevent ->
            //If the keyevent is a key-down event on the "enter" button
            if (keyevent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                // Perform your action on key press here
                viewModel.scanStock(binding.edtScanStock.text.toString())
                true
            } else false
        }

        viewModel.getBoxDataLive.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    binding.edtBoxCode.setText(it.data!!.code)
                    binding.actJewelleryType.setText(it.data!!.jewelleryType)
                    binding.edtScanBox.text?.clear()
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()

                }
            }
        }

        viewModel.scanStockLive.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    loadingDialog.show()
                }
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    //id list
                    val resultItem = StockCodeForListUiModel(
                        it.data!!.id,
                        binding.edtScanStock.text.toString(),
                    )
                    if (viewModel.stockCodeList.contains(resultItem).not()){
                        viewModel.addStockCode(resultItem)
                    }else{
                        Toast.makeText(requireContext(),"Stock Already Scanned",Toast.LENGTH_LONG).show()
                    }
                    viewModel.resetScanStockLive()
//                    findNavController().navigate(CollectStockFragmentDirections.actionCollectStockFragmentToFillInfoCollectStockFragment())
                }
                is Resource.Error -> {
                    loadingDialog.dismiss()
                }
            }
        }
        viewModel.stockListLive.observe(viewLifecycleOwner){
            binding.includeButton.btnMatch.isEnabled = it.size>0
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
            binding.edtScanStock.text?.clear()
        }

        binding.includeButton.btnMatch.setOnClickListener {
            if (viewModel.targetBoxCode == null){
                Toast.makeText(requireContext(),"Please scan a box",Toast.LENGTH_LONG).show()
            }else{
                findNavController().navigate(TransferCheckUpStockFragmentDirections.actionTransferCheckUpStockFragmentToMatchCodeFragment(
                    viewModel.stockCodeList.map { it.invoiceCode }.toTypedArray(),viewModel.targetBoxCode!!,viewModel.stockCodeList.map { it.id }.toTypedArray()))
                viewModel.resetStockListLive()
                binding.edtBoxCode.text?.clear()
                binding.actJewelleryType.text?.clear()
            }

        }

    }
}
