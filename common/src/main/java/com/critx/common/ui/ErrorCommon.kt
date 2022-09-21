package com.critx.common.ui

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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

fun ImageView.getThumbnail(url: String){
    val requestOptions = RequestOptions()
    Glide.with(context)
        .load(url)
        .dontAnimate()
        .apply(requestOptions)
        .thumbnail(Glide.with(context).load(url))
        .into(this)
}

fun getBitMapWithGlide(url: String?,context: Context) =Glide.with(context).asBitmap().load(url).submit().get()

fun AutoCompleteTextView.showDropdown(adapter: ArrayAdapter<String>?) {
    if (!TextUtils.isEmpty(this.text.toString())) {
        adapter?.filter?.filter(null)
    }
}
