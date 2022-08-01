package com.critx.shwemiAdmin.screens.collectstock.fillinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.critx.shwemiAdmin.databinding.FragmentCollectStockBinding
import com.critx.shwemiAdmin.databinding.FragmentFillInfoCollectStockBinding
import com.google.android.material.textfield.TextInputLayout

class FillInfoCollectStockFragment:Fragment() {
    private lateinit var binding:FragmentFillInfoCollectStockBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentFillInfoCollectStockBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    fun makeEnableTil(view:TextInputLayout,isChecked:Boolean){
        view.isEnabled = isChecked
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cbWastage.setOnCheckedChangeListener { compoundButton, ischecked ->
            makeEnableTil(binding.tilK,ischecked)
            makeEnableTil(binding.tilP,ischecked)
            makeEnableTil(binding.tilY,ischecked)
        }
        binding.cbGoldsmith.setOnCheckedChangeListener { compoundButton, ischecked ->
            makeEnableTil(binding.tilChooseOneGoldSmith,ischecked)
        }
        binding.cbBonus.setOnCheckedChangeListener { compoundButton, ischecked ->
            makeEnableTil(binding.tilEnterBonusAmount,ischecked)
        }
        binding.cbAmount.setOnCheckedChangeListener { compoundButton, ischecked ->
            makeEnableTil(binding.tilEnterAmount,ischecked)
        }

    }
}