package com.critx.shwemiAdmin.screens.sampleTakeAndReturn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.critx.common.ui.getAlertDialog
import com.critx.commonkotlin.util.Resource
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.CustomTabItemBinding
import com.critx.shwemiAdmin.databinding.FragmentSampleTakeAndReturnBinding
import com.critx.shwemiAdmin.screens.setupStock.SharedViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleTakeAndReturn : Fragment() {
    private lateinit var binding: FragmentSampleTakeAndReturnBinding
    private val viewModel by viewModels<SampleTakeAndReturnViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private lateinit var loadingDialog: AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSampleTakeAndReturnBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    private fun toolbarsetup() {

        val toolbarCenterImage: ImageView =
            activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView =
            activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible = true
        toolbarCenterText.text = getString(R.string.sample_take_amp_return)
        toolbarCenterImage.isVisible = false
        toolbarEndIcon.isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        loadingDialog = requireContext().getAlertDialog()
//        viewModel.getHandedList()

//        viewModel.getHandedListLiveData.observe(viewLifecycleOwner) {
//            when (it) {
//                is Resource.Loading -> {
//                    loadingDialog.show()
//                }
//                is Resource.Success -> {
//                    loadingDialog.dismiss()
//                    val toolbarEndIcon: ImageView =
//                        activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
//                    if (it.data.isNullOrEmpty().not()) {
//                        toolbarEndIcon.setImageDrawable(
//                            requireContext().getDrawable(R.drawable.cart_box_red)
//                        )
//                    }else{
//                        toolbarEndIcon.setImageDrawable(
//                            requireContext().getDrawable(R.drawable.cart_box)
//                        )
//                    }
//                    toolbarEndIcon.setOnClickListener {
//                        findNavController().navigate(SampleTakeAndReturnDirections.actionSampleTakeAndReturnFragmentToHandedListFragment())
//                    }
//
//                }
//                is Resource.Error -> {
//                    loadingDialog.dismiss()
//                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
//
//                }
//            }
//        }
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
                    customTab.tvTabItem.text = "Inventory"
                }
                1 -> {
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