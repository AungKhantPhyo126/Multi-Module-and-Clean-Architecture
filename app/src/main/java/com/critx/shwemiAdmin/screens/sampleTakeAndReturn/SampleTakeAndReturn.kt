package com.critx.shwemiAdmin.screens.sampleTakeAndReturn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.CustomTabItemBinding
import com.critx.shwemiAdmin.databinding.FragmentSampleTakeAndReturnBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.sample_take_amp_return)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible =true
        toolbarEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.cart_box))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()

        binding.vpSampleTakeAndReturn.adapter = SampleTakeAndReturnPagerAdapter(this)
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
                    customTab.tvTabItem.text = "Inventory"
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