package com.critx.shwemiAdmin.screens.setupStock.third.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.critx.shwemiAdmin.databinding.FragmentNewGroupBinding
import com.critx.shwemiAdmin.databinding.ItemAddNewBinding

class EditGroupFragment:Fragment() {
    private lateinit var binding: FragmentNewGroupBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentNewGroupBinding.inflate(inflater).also {
            binding=it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}