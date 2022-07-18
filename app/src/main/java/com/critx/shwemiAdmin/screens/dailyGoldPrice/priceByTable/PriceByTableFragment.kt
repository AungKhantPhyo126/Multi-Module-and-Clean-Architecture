package com.critx.shwemiAdmin.screens.dailyGoldPrice.priceByTable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.critx.common.ui.showSuccessDialog
import com.critx.shwemiAdmin.databinding.FragmentPriceByTableBinding


class PriceByTableFragment: Fragment() {
    private lateinit var binding: FragmentPriceByTableBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentPriceByTableBinding.inflate(inflater).also {
            binding=it
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnUpdate.setOnClickListener {
            requireContext().showSuccessDialog("Price Uploaded") {
                findNavController().popBackStack()
            }
        }
        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}