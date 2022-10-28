package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.voucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.critx.common.qrscan.getBarLauncher
import com.critx.common.qrscan.scanQrCode
import com.critx.shwemiAdmin.databinding.FragmentVoucherBinding
import com.critx.shwemiAdmin.screens.sampleTakeAndReturn.GIVE_GOLD_STATE
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VoucherFragment:Fragment() {
    private lateinit var binding: FragmentVoucherBinding
    private lateinit var barlauncer:Any
    private val viewModel by viewModels<VoucherViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()


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

        binding.layoutBtnGroup.btnAddToHandedList.isEnabled =
            sharedViewModel.sampleTakeScreenUIState != GIVE_GOLD_STATE
        binding.tilScanHere.setEndIconOnClickListener {
            this.scanQrCode(requireContext(),barlauncer)
        }
        val voucherRecyclerAdapter = VoucherRecyclerAdapter{

        }

        binding.rvVoucherList.adapter = voucherRecyclerAdapter


    }

}