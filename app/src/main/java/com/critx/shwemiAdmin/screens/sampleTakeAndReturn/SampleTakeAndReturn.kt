package com.critx.shwemiAdmin.screens.sampleTakeAndReturn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.critx.common.databinding.ShwemiSuccessDialogBinding
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.CustomTabItemBinding
import com.critx.shwemiAdmin.databinding.FragmentSampleTakeAndReturnBinding
import com.google.android.material.tabs.TabLayoutMediator

class SampleTakeAndReturn : Fragment() {
    private lateinit var binding: FragmentSampleTakeAndReturnBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSampleTakeAndReturnBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vpSampleTakeAndReturn.adapter = SampleTakeAndReturnPagerAdapter(this)
        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
        TabLayoutMediator(
            binding.tlSampleTakeAndReturn,
            binding.vpSampleTakeAndReturn
        ) { tab, position ->
            when (position) {
                0 -> {
                    val customTab = CustomTabItemBinding.inflate(
                        layoutInflater, ConstraintLayout(requireContext()), false
                    )
                    tab.customView = customTab.root
                    customTab.tvTabItem.text = "Voucher"
                }
                1 -> {
                    val customTab = CustomTabItemBinding.inflate(
                        layoutInflater, ConstraintLayout(requireContext()), false
                    )
                    tab.customView = customTab.root
                    customTab.tvTabItem.text = "New"
                }
                2 -> {
                    val customTab = CustomTabItemBinding.inflate(
                        layoutInflater, ConstraintLayout(requireContext()), false
                    )
                    tab.customView = customTab.root
                    customTab.tvTabItem.text = "Outside"
                }
            }
        }.attach()
    }
}