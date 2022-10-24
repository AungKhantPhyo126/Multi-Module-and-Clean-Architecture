package com.critx.shwemiAdmin.screens.giveGold

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.critx.common.databinding.ShwemiSuccessDialogBinding
import com.critx.common.ui.showSuccessDialog
import com.critx.shwemiAdmin.R
import com.critx.shwemiAdmin.databinding.FragmentGiveGoldBinding
import com.critx.shwemiAdmin.databinding.ServiceChargeDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GiveGoldFragment: Fragment() {
    private lateinit var binding:FragmentGiveGoldBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentGiveGoldBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    private fun toolbarsetup(){

        val toolbarCenterImage: ImageView = activity!!.findViewById<View>(R.id.center_image) as ImageView
        val toolbarCenterText: TextView = activity!!.findViewById<View>(R.id.center_text_title) as TextView
        val toolbarEndIcon: ImageView = activity!!.findViewById<View>(R.id.iv_end_icon) as ImageView
        toolbarCenterText.isVisible=true
        toolbarCenterText.text=getString(R.string.give_gold)
        toolbarCenterImage.isVisible =false
        toolbarEndIcon.isVisible =false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarsetup()
        binding.mcvHundredPercent.setOnClickListener {
            binding.mcvHundredPercent.isChecked = binding.mcvHundredPercent.isChecked.not()
        }
        binding.btnConfirm.setOnClickListener {
            requireContext().showServiceChargeDialog {
                requireContext().showSuccessDialog("Gold is given to the GoldSmith"){

                }
            }
        }
    }
}

fun Context.showServiceChargeDialog(onClick: () -> Unit) {
    val builder = MaterialAlertDialogBuilder(this)
    val inflater: LayoutInflater = LayoutInflater.from(builder.context)
    val alertDialogBinding = ServiceChargeDialogBinding.inflate(
        inflater, ConstraintLayout(builder.context), false
    )
    builder.setView(alertDialogBinding.root)
    val alertDialog = builder.create()
    alertDialog.setCancelable(false)
    alertDialogBinding.btnGive.setOnClickListener {
        alertDialog.dismiss()
        onClick()
    }
    alertDialogBinding.ivCross.setOnClickListener {
        alertDialog.dismiss()
    }
    alertDialog.show()
}