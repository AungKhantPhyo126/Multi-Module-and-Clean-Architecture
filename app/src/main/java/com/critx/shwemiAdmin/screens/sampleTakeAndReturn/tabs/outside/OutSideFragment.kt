package com.critx.shwemiAdmin.screens.sampleTakeAndReturn.tabs.outside

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.critx.common.qrscan.getBarLauncher
import com.critx.shwemiAdmin.databinding.FragmentOutsideBinding
import com.critx.shwemiAdmin.databinding.NewSampleFragmentBinding

class OutSideFragment:Fragment() {
    private lateinit var binding: FragmentOutsideBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentOutsideBinding.inflate(inflater).also {
            binding = it
        }.root
    }
}