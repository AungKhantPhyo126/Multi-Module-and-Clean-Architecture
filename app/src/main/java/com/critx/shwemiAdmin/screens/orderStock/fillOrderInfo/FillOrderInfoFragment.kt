package com.critx.shwemiAdmin.screens.orderStock.fillOrderInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.critx.shwemiAdmin.databinding.FragmentFillOrderInfoBinding
import com.critx.shwemiAdmin.screens.transferCheckUpStock.checkup.StockRecyclerAdapter
import com.critx.shwemiAdmin.uiModel.StockCodeForListUiModel

class FillOrderInfoFragment:Fragment() {
    private lateinit var binding:FragmentFillOrderInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentFillOrderInfoBinding.inflate(inflater).also {
            binding=it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = StockInfoRecyclerAdapter()
        binding.rvStockInfo.adapter = adapter
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
    }
}