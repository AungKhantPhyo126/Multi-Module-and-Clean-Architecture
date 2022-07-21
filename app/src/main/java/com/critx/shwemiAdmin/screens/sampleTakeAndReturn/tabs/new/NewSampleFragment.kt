package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.new

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.critx.common.qrscan.getBarLauncher
import com.critx.common.qrscan.scanQrCode
import com.critx.shwemiAdmin.databinding.NewSampleFragmentBinding
import com.critx.shwemiAdmin.uiModel.sampleTakeAndReturn.NewSampleUIModel

class NewSampleFragment:Fragment() {
    private lateinit var binding: NewSampleFragmentBinding
    private lateinit var barlauncer:Any
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        barlauncer = this.getBarLauncher(requireContext())
        return NewSampleFragmentBinding.inflate(inflater).also {
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
            NewSampleUIModel("1",1234566778,"test 1"),
            NewSampleUIModel("2",1234566778,"test 2"),
            NewSampleUIModel("3",1234566778,"test 3")


        ))

    }
}