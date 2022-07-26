package com.critx.shwemiAdmin.screens.confirmVoucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.critx.shwemiAdmin.databinding.FragmentConfirmVoucherBinding

class ConfirmVoucherFragment:Fragment() {
    private lateinit var binding: FragmentConfirmVoucherBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentConfirmVoucherBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ibEdit.setOnClickListener {
            findNavController().navigate(ConfirmVoucherFragmentDirections.actionConfirmVoucherFragmentToUnConfirmVoucherFragment())
        }
    }
}