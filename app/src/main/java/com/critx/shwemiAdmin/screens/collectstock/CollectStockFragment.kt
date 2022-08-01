package com.critx.shwemiAdmin.screens.collectstock

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
import com.critx.common.qrscan.getBarLauncher
import com.critx.common.qrscan.getBarLauncherTest
import com.critx.common.qrscan.scanQrCode
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentCollectStockBinding
import com.critx.shwemiAdmin.screens.discount.DiscountRecyclerAdapter

class CollectStockFragment:Fragment() {
    private lateinit var binding: FragmentCollectStockBinding
    private val viewModel by viewModels<CollectStockVIewModel>()
    private lateinit var barlauncer:Any
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentCollectStockBinding.inflate(inflater).also {
            binding = it
        }.root
    }
    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.collect_stock_and_give_data)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible =false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        barlauncer = this.getBarLauncherTest(requireContext()) { viewModel.addStockCode(it) }
        binding.mcvScanHere.setOnClickListener {
            scanQrCode(requireContext(),barlauncer)
        }
        val adapter = CollectStockRecyclerAdapter{

        }
        binding.mcvScanHere.setOnClickListener {
            scanQrCode(requireContext(),barlauncer)
        }
        binding.layoutCollectStockBatch.btnNext.setOnClickListener {
            findNavController().navigate(CollectStockFragmentDirections.actionCollectStockFragmentToFillInfoCollectStockFragment())
        }
        binding.layoutCollectStockBatch.rvCollectStockBatch.adapter=adapter
        viewModel.scannedStockcodebatch.observe(viewLifecycleOwner){
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }

        binding.chiptGp.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.contains(R.id.chip_single)){
                binding.singleSelectedLayout.isVisible=true
                binding.layoutCollectStockBatch.root.isVisible=false
            }else if (checkedIds.contains(R.id.chip_batch)){
                binding.singleSelectedLayout.isVisible=false
                binding.layoutCollectStockBatch.root.isVisible=true
            }
        }
    }
}