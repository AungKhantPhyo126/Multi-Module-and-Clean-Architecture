package com.critx.shwemiAdmin.screens.setupStock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.ChoiceChipBinding
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
            arrayListOf("Extra Soft", "Soft", "Medium", "Hard", "Extra Hard")
        val chipIdList = mutableListOf<Int>()
        for (name in nameList) {
            val chip = createChip(name)

            binding.chipGroupJewelleryType.addView(chip)
        }

        binding.chipGroupJewelleryType.setOnCheckedStateChangeListener { group, checkedIds ->
           for (index in 0 until group.childCount){
               val chip = group[index] as Chip
               if (chip.isChecked){
                   binding.tvFirstCat.setTextColor(requireContext().getColor(R.color.primary_color))
                   binding.tvFirstCat.text = chip.text
               }
           }
        }

    }
    private fun createChip(label: String): Chip {
        val chip = ChoiceChipBinding.inflate(layoutInflater).root
        chip.text = label
        return chip
    }

}

data class CustomChip(
    val id:String,
    val name:String
)