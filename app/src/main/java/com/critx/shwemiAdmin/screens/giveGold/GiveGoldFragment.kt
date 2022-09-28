package com.critx.shwemiAdmin.screens.giveGold

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.critx.common.databinding.ShwemiSuccessDialogBinding
import com.critx.common.ui.showSuccessDialog
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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