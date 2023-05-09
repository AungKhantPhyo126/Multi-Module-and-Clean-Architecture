package com.critx.shwemiAdmin.screens.discount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.critx.common.qrscan.getBarLauncher
import com.critx.common.qrscan.scanQrCode
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentDiscountBinding
import com.critx.shwemiAdmin.screens.flashsale.FlashSaleViewModel
import com.critx.shwemiAdmin.uiModel.discount.DiscountUIModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscountFragment :Fragment(){
    private lateinit var binding: FragmentDiscountBinding
    private lateinit var barlauncer:Any
    private val viewModel by viewModels<DiscountViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        barlauncer = this.getBarLauncher(requireContext())

        return FragmentDiscountBinding.inflate(inflater).also {
            binding = it
        }.root
    }
    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.discount)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible =false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        binding.layoutBtnGroup.tilTitle.isVisible = false
        val adapter = DiscountRecyclerAdapter{

        }
        binding.mcvScanHere.setOnClickListener {
            scanQrCode(requireContext(),barlauncer)

        }
        binding.layoutDiscount.rvStockCodeList.adapter=adapter

    }
}
