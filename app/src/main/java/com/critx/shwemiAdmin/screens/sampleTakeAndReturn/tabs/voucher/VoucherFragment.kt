package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.voucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.critx.common.qrscan.getBarLauncher
import com.critx.common.qrscan.scanQrCode
import com.critx.shwemiAdmin.databinding.FragmentVoucherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VoucherFragment:Fragment() {
    private lateinit var binding: FragmentVoucherBinding
    private lateinit var barlauncer:Any
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        barlauncer = this.getBarLauncher(requireContext())
        return FragmentVoucherBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mcvScanHere.setOnClickListener {
            this.scanQrCode(requireContext(),barlauncer)
        }
        val voucherRecyclerAdapter = VoucherRecyclerAdapter{

        }

        binding.rvVoucherList.adapter = voucherRecyclerAdapter

    }

}