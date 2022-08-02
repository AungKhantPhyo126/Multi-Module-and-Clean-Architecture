package com.critx.shwemiAdmin.screens.setupStock.first

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.critx.common.ui.createChip
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentSetUpStocklBinding
import com.google.android.material.chip.Chip


class SetupStockFragment:Fragment() {
    private lateinit var binding: FragmentSetUpStocklBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSetUpStocklBinding.inflate(inflater).also {
            binding = it
        }.root
    }
    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.set_up_stock)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible =false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        val nameList =
            arrayListOf("Extra Soft", "Soft", "Medium", "Hard", "Extra Hard",
                "Extra Soft", "Soft", "Medium", "Hard", "Extra Hard")
        val chipIdList = mutableListOf<Int>()
        for (name in nameList) {
            val chip = requireContext().createChip(name)

            binding.chipGroupJewelleryType.addView(chip)
        }

        binding.chipGroupJewelleryType.setOnCheckedStateChangeListener { group, checkedIds ->
            for (index in 0 until group.childCount){
                val chip = group[index] as Chip
                if (chip.isChecked){
                    binding.tvFirstCat.setTextColor(requireContext().getColor(R.color.primary_color))
                    binding.tvFirstCat.text = chip.text
                    binding.ivFirst.isVisible = true
                }
            }
        }
        binding.btnNext.setOnClickListener {
            if (binding.tvFirstCat.text != requireContext().getString(R.string.jewellery_type)){
                findNavController().navigate(SetupStockFragmentDirections.actionSetupStockFragmentToChooseJewelleryQualityFragment(
                    binding.tvFirstCat.text.toString()
                ))
            }else{
                Toast.makeText(requireContext(),"Please choose at least one category",Toast.LENGTH_LONG).show()
            }

        }

    }


}

data class CustomChip(
    val id:String,
    val name:String
)