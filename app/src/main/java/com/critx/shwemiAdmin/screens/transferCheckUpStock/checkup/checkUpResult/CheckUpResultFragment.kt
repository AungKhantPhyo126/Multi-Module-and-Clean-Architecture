package com.critx.shwemiAdmin.screens.transferCheckUpStock.checkup.checkUpResult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.critx.shwemiAdmin.databinding.FragmentCheckUpBinding
import com.critx.shwemiAdmin.databinding.FragmentCheckUpResultBinding

class CheckUpResultFragment: Fragment() {
    private lateinit var binding: FragmentCheckUpResultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentCheckUpResultBinding.inflate(inflater).also {
            binding=it
        }.root
    }
}