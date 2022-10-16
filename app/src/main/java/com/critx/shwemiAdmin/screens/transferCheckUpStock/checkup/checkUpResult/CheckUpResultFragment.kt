package com.critx.shwemiAdmin.screens.transferCheckUpStock.checkup.checkUpResult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentCheckUpBinding
import com.critx.shwemiAdmin.databinding.FragmentCheckUpResultBinding
import com.critx.shwemiAdmin.screens.transferCheckUpStock.checkup.StockRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckUpResultFragment: Fragment() {
    private lateinit var binding: FragmentCheckUpResultBinding
    private val args by navArgs<CheckUpResultFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentCheckUpResultBinding.inflate(inflater).also {
            binding=it
        }.root
    }
    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text="Check Up"
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible =false
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        val adapter = CheckUpResultRecyclerAdapter{

        }
        binding.layoutScannedStockList.rvStockCodeList.adapter = adapter
        adapter.submitList(args.checkUpStocks.notFromBox)
        binding.layoutScannedStockList.tvTotalScannedStockCount.text = args.checkUpStocks.notFromBox.size.toString()

        binding.chiptGp.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.contains(binding.chipRequired.id)){
                binding.layoutScannedStockList.tvTotalScannedStockCount.text = args.checkUpStocks.required.size.toString()
                adapter.submitList(args.checkUpStocks.required)
            }else{
                binding.layoutScannedStockList.tvTotalScannedStockCount.text = args.checkUpStocks.notFromBox.size.toString()
                adapter.submitList(args.checkUpStocks.notFromBox)
            }
        }
        binding.btnConfirm.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}