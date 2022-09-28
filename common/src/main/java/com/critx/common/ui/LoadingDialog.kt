package com.critx.common.ui

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.critx.common.databinding.LoadingDialogBinding
import com.critx.common.databinding.ShwemiSuccessDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
//
//fun showLoadingDialog():AlertDialog {
//    val builder = MaterialAlertDialogBuilder(this)
//    val inflater: LayoutInflater = LayoutInflater.from(builder.context)
//    val alertDialogBinding = LoadingDialogBinding.inflate(
//        inflater, ConstraintLayout(builder.context), false
//    )
//    builder.setView(alertDialogBinding.root)
//    val alertDialog = builder.create()
//    alertDialog.show()
//    return alertDialog
//}
//
//fun Context.appearDialog(isloading: Boolean){
//    if (isloading){
//        showLoadingDialog()
//    }else{
//        showLoadingDialog().dismiss()
//    }
//
//}
fun Context.getAlertDialog():AlertDialog{
    val builder = MaterialAlertDialogBuilder(this)
    val inflater: LayoutInflater = LayoutInflater.from(builder.context)
    val alertDialogBinding = LoadingDialogBinding.inflate(
        inflater, ConstraintLayout(builder.context), false
    )
    builder.setView(alertDialogBinding.root)
    val alertDialog = builder.create()
    alertDialog.setCancelable(false)
    return alertDialog
}
