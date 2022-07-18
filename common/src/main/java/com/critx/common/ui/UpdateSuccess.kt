package com.critx.common.ui

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.critx.common.databinding.ShwemiSuccessDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

//fun Context.showSuccessDialog(successMessage:String){
//    MaterialAlertDialogBuilder(this).setTitle(successMessage)
//        .setPositiveButton("OK"){dialog,_->
//            dialog.dismiss()
//        }
//        .show()
//}

fun Context.showSuccessDialog( message: String, onClick: () -> Unit) {
    val builder = MaterialAlertDialogBuilder(this)
    val inflater: LayoutInflater = LayoutInflater.from(builder.context)
    val alertDialogBinding = ShwemiSuccessDialogBinding.inflate(
        inflater, ConstraintLayout(builder.context), false
    )
    builder.setView(alertDialogBinding.root)
    val alertDialog = builder.create()
    alertDialogBinding.tvMessage.text = message
    alertDialogBinding.btnOk.setOnClickListener {
        alertDialog.dismiss()
        onClick()
    }
    alertDialog.show()
}