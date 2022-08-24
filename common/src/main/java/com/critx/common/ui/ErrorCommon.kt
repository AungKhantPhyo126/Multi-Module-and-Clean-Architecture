package com.critx.common.ui

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.critx.common.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.showErrorCommonUI(errorMessage:String){
    MaterialAlertDialogBuilder(this).setTitle(errorMessage).show()
}

fun ImageView.loadImageWithGlide(url:String?){
    url?.let {
        Glide.with(this).load(it).apply(
            RequestOptions.placeholderOf(R.drawable.loading_animation)
                .override(100,100)
                .error(R.drawable.profile_avatar)
        ).into(this)
    }
}