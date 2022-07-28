package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.voucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.critx.common.qrscan.getBarLauncher
import com.critx.common.qrscan.scanQrCode
import com.critx.shwemiAdmin.databinding.FragmentVoucherBinding
import com.critx.shwemiAdmin.databinding.NewSampleFragmentBinding
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.new.NewSampleRecyclerAdapter
import com.critx.shwemiAdmin.uiModel.sampleTakeAndReturn.NewSampleUIModel
import com.critx.shwemiAdmin.uiModel.sampleTakeAndReturn.VoucherUIModel

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
        voucherRecyclerAdapter.submitList(listOf(
            VoucherUIModel("1",1234566778,"test 1"),
            VoucherUIModel("2",1234566778,"test 2"),
            VoucherUIModel("3",1234566778,"test 3")

        ))
    }

}