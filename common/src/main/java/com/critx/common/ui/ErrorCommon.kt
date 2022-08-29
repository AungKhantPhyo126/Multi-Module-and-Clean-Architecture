package com.critx.common.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.critx.common.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.showErrorCommonUI(errorMessage:String){
    MaterialAlertDialogBuilder(this).setTitle(errorMessage).show()
}

fun ImageView.loadImageWithGlide(url:String?){
    url?.let {
        Glide.with(this).asBitmap().load(it).apply(
            RequestOptions.placeholderOf(R.drawable.loading_animation)
                .override(100,100)
                .error(R.drawable.profile_avatar)
        ).into(this)
    }
}
fun ImageView.loadImageWithGlideReady(url:String?):Bitmap?{
    var bm:Bitmap? = null
    url?.let {
        Glide.with(this).asBitmap().load(it).apply(
            RequestOptions.placeholderOf(R.drawable.loading_animation)
                .override(100,100)
                .error(R.drawable.profile_avatar)
        ).listener(object :RequestListener<Bitmap>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                bm = resource
                return true
            }

        })
            .into(this)
    }
    return bm
}

fun getBitMapWithGlide(url: String?,context: Context) =Glide.with(context).asBitmap().load(url).submit().get()
