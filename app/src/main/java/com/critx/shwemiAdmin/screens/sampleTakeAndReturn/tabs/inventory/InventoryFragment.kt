package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.critx.common.qrscan.getBarLauncher
import com.critx.common.qrscan.scanQrCode
import com.critx.shwemiAdmin.databinding.FragmentInventoryBinding
import com.critx.shwemiAdmin.uiModel.simpleTakeAndReturn.SampleItemUIModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InventoryFragment:Fragment() {
    private lateinit var binding: FragmentInventoryBinding
    private lateinit var barlauncer:Any
//    private val viewModel by viewModels<InventoryViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        barlauncer = this.getBarLauncher(requireContext())
        return FragmentInventoryBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mcvScanHere.setOnClickListener {
            this.scanQrCode(requireContext(),barlauncer)
        }
        val newSampleRecyclerAdapter = NewSampleRecyclerAdapter{

        }

        binding.layoutSampleLists.rvSample.adapter = newSampleRecyclerAdapter
        newSampleRecyclerAdapter.submitList(listOf(
            SampleItemUIModel("1","1234566778","test 1","ok pr"),
            SampleItemUIModel("2","1234566778","test 2","d lo loke"),
            SampleItemUIModel("3","1234566778","test 3","ho lo loke")


        ))

    }
}