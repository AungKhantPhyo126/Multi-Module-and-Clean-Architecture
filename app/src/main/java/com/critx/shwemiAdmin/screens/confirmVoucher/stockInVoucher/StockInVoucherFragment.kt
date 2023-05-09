package com.critx.shwemiAdmin.screens.confirmVoucher.stockInVoucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.critx.shwemiAdmin.databinding.FragmentConfirmVoucherBinding
import com.critx.shwemiAdmin.databinding.FragmentStockInVoucherBinding

class StockInVoucherFragment :Fragment() {
    private lateinit var binding: FragmentStockInVoucherBinding
    private val args by navArgs<StockInVoucherFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentStockInVoucherBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = StockInVoucherRecyclerAdapter()
        binding.rvVoucherList.adapter = adapter
        adapter.submitList(args.stockCodeList.toList())
        binding.tvStockListSize.text = args.stockCodeList.size.toString()
    }
}