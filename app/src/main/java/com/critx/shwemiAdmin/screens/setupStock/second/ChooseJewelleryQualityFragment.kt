package com.critx.shwemiAdmin.screens.setupStock.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.critx.common.ui.createChip
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentChooseJewelleryQualityBinding
import com.google.android.material.chip.Chip

class ChooseJewelleryQualityFragment:Fragment() {
    private lateinit var binding: FragmentChooseJewelleryQualityBinding
    private val args by navArgs<ChooseJewelleryQualityFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentChooseJewelleryQualityBinding.inflate(inflater).also {
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
        binding.tvFirstCat.text = args.firstCat
        val nameList =
            arrayListOf("Extra Soft", "Soft", "Medium", "Hard", "Extra Hard",
                "Extra Soft", "Soft", "Medium", "Hard", "Extra Hard")
        val chipIdList = mutableListOf<Int>()
        for (name in nameList) {
            val chip = requireContext().createChip(name)

            binding.chipGroupJewelleryQuality.addView(chip)
        }

        binding.chipGroupJewelleryQuality.setOnCheckedStateChangeListener { group, checkedIds ->
            for (index in 0 until group.childCount){
                val chip = group[index] as Chip
                if (chip.isChecked){
                    binding.tvSecondCat.isVisible=true
                    binding.tvSecondCat.setTextColor(requireContext().getColor(R.color.primary_color))
                    binding.tvSecondCat.text = chip.text
                    binding.ivSecond.isVisible = true
                }
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }
}