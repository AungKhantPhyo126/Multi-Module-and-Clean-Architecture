package com.critx.shwemiAdmin.screens.transferCheckUpStock.transfer.matchCode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.critx.shwemiAdmin.databinding.FragmentMatchCodeBinding

class MatchCodeFragment :Fragment() {
    private lateinit var binding:FragmentMatchCodeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentMatchCodeBinding.inflate(inflater).also {
            binding = it
        }.root
    }
}