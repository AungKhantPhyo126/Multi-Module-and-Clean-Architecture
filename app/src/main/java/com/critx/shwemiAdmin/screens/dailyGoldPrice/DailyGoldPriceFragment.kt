package com.critx.shwemiAdmin.screens.dailyGoldPrice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.critx.common.ui.showSuccessDialog
import com.critx.shwemiAdmin.databinding.FragmentDailyGoldPriceBinding

class DailyGoldPriceFragment:Fragment() {
    private lateinit var binding:FragmentDailyGoldPriceBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentDailyGoldPriceBinding.inflate(inflater).also {
            binding=it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnByTable.setOnClickListener {
            findNavController().navigate(DailyGoldPriceFragmentDirections.actionDailyGoldPriceFragmentToPriceByTableFragment())
        }
        binding.btnUpdate.setOnClickListener {
            requireContext().showSuccessDialog("Price Uploaded") {
                findNavController().popBackStack()
            }
        }
    }
}