package com.critx.shwemiAdmin.screens.editStock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.critx.shwemiAdmin.databinding.FragmentEditStockBinding

class EditStockFragment:Fragment() {
    private lateinit var binding:FragmentEditStockBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentEditStockBinding.inflate(inflater).also {
            binding = it
        }.root
    }
}