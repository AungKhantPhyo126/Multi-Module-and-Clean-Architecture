package com.critx.shwemiAdmin.screens.transferCheckUpStock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.CustomTabItemBinding
import com.critx.shwemiAdmin.databinding.FragmentSampleTakeAndReturnBinding
import com.critx.shwemiAdmin.databinding.FragmentTransferCheckUpStockBinding
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.SampleTakeAndReturnPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class TransferCheckUpStockFragment:Fragment() {
    private lateinit var binding:FragmentTransferCheckUpStockBinding
    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.transfer_checkup_stock)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible =false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentTransferCheckUpStockBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()

        binding.vpTransferCheckUpStock.adapter = SampleTakeAndReturnPagerAdapter(this)
        TabLayoutMediator(
            binding.tlTransferCheckUpStock,
            binding.vpTransferCheckUpStock
        ) { tab, position ->
            when (position) {
                0 -> {
                    val customTab = CustomTabItemBinding.inflate(
                        layoutInflater, ConstraintLayout(requireContext()), false
                    )
                    tab.customView = customTab.root
                    customTab.tvTabItem.text = "Checkup"
                }
                1 -> {
                    val customTab = CustomTabItemBinding.inflate(
                        layoutInflater, ConstraintLayout(requireContext()), false
                    )
                    tab.customView = customTab.root
                    customTab.tvTabItem.text = "Transfer"
                }
            }
        }.attach()
    }
}