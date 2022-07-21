package com.critx.shwemiAdmin.screens.confirmVoucher.unconfirmVoucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.critx.shwemiAdmin.databinding.FragmentUnconfirmedVoucherBinding
import com.critx.shwemiAdmin.uiModel.UnConfirmVoucherUIModel

class UnConfirmVoucherFragment : Fragment() {
    private lateinit var binding: FragmentUnconfirmedVoucherBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentUnconfirmedVoucherBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
        val adapter = UnConfirmVoucherRecyclerAdapter()
        binding.rvUnconfirmVoucher.adapter = adapter
        adapter.submitList(
            listOf(
                UnConfirmVoucherUIModel(
                    "1", 2904220001, 2904220001, 2904220001
                ),
                UnConfirmVoucherUIModel(
                    "2", 2904220001, 2904220001, 2904220001
                ),
                UnConfirmVoucherUIModel(
                    "3", 2904220001, 2904220001, 2904220001
                ),
                UnConfirmVoucherUIModel(
                    "4", 2904220001, 2904220001, 2904220001
                ),
                UnConfirmVoucherUIModel(
                    "5", 2904220001, 2904220001, 2904220001
                ),
                UnConfirmVoucherUIModel(
                    "6", 2904220001, 2904220001, 2904220001
                ),
                UnConfirmVoucherUIModel(
                    "7",2904220001,2904220001,2904220001
                ),
                UnConfirmVoucherUIModel(
                    "8",2904220001,2904220001,2904220001
                ),
                UnConfirmVoucherUIModel(
                    "9",2904220001,2904220001,2904220001
                ),
                UnConfirmVoucherUIModel(
                    "10",2904220001,2904220001,2904220001
                ),
                UnConfirmVoucherUIModel(
                    "11",2904220001,2904220001,2904220001
                )
            )
        )
    }
}