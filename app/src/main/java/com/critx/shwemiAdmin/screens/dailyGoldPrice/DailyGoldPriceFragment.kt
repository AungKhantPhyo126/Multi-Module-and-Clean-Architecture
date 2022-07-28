package com.critx.shwemiAdmin.screens.dailyGoldPrice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.critx.common.ui.showSuccessDialog
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentDailyGoldPriceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DailyGoldPriceFragment:Fragment() {
    private lateinit var binding:FragmentDailyGoldPriceBinding
    private val viewModel by viewModels<DailyGoldPriceViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentDailyGoldPriceBinding.inflate(inflater).also {
            binding=it
        }.root
    }

    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.daily_gold_price)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_logout))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        if (!viewModel.isLogin()){
            findNavController().navigate(DailyGoldPriceFragmentDirections.actionDailyGoldPriceFragmentToLoginFragment())
        }
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