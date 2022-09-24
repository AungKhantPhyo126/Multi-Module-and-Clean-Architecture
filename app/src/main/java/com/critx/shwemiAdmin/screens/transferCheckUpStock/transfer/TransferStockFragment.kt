package com.critx.shwemiAdmin.screens.transferCheckUpStock.transfer

import com.critx.shwemiAdmin.screens.transferCheckUpStock.checkup.StockRecyclerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.critx.common.qrscan.getBarLauncher
import com.critx.common.qrscan.scanQrCode
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentCheckUpBinding
import com.critx.shwemiAdmin.databinding.FragmentTransferStockBinding
import com.critx.shwemiAdmin.screens.transferCheckUpStock.TransferCheckUpStockFragmentDirections
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel
import com.critx.shwemiAdmin.uiModel.discount.DiscountUIModel

class TransferStockFragment:Fragment() {
    private lateinit var binding:FragmentTransferStockBinding
    private lateinit var barlauncer:Any

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        barlauncer = this.getBarLauncher(requireContext())

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
        binding.ivScanStock.setOnClickListener {
            scanQrCode(requireContext(),barlauncer)
        }
        val adapter = StockRecyclerAdapter{

        }
        binding.includeScannedStockList.rvStockCodeList.adapter = adapter
        adapter.submitList(listOf(
            StockCodeForListUiModel(
                "1",
                "123456788"
            ),
            StockCodeForListUiModel(
                "2",
                "123456788"
            ),
            StockCodeForListUiModel(
                "3",
                "123456788"
            ),
            StockCodeForListUiModel(
                "4",
                "123456788"
            )
        ))
        binding.includeButton.btnMatch.setOnClickListener {
            findNavController().navigate(TransferCheckUpStockFragmentDirections.actionTransferCheckUpStockFragmentToMatchCodeFragment())
        }
    }
}
