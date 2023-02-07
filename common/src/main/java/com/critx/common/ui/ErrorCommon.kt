package com.critx.common.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.critx.common.R
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel


fun Context.showErrorCommonUI(errorMessage:String){
    MaterialAlertDialogBuilder(this).setTitle(errorMessage).show()
}

fun ImageView.loadImageWithGlide(url:String?){
    url?.let {
        Glide.with(this).asBitmap().load(it).apply(
            RequestOptions.placeholderOf(R.drawable.loading_animation)
                .error(R.drawable.profile_avatar)
        ).into(this)
    }
}

fun ImageView.loadImageWithGlideWithUri(uri:Uri?){
    uri?.let {
        Glide.with(this).asBitmap().load(it).apply(
            RequestOptions.placeholderOf(R.drawable.loading_animation)
                .dontTransform()
                .error(R.drawable.profile_avatar)
        ).into(this)
    }
}

fun ImageView.loadImageWithGlideAsGif(url:String?){
    url?.let {
        Glide.with(this).asGif().load(it).apply(
            RequestOptions.placeholderOf(R.drawable.loading_animation)
                .dontTransform()
                .error(R.drawable.profile_avatar)

        ).into(this)
    }
}

fun ImageView.loadImageWithGlideAsGifFromStorage(file:File?){
    file?.let {
        Glide.with(this).asGif().load(it).apply(
            RequestOptions.placeholderOf(R.drawable.loading_animation)
                .dontTransform()
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
fun getGifWithGlide(url: String?,context: Context) =Glide.with(context).asGif().load(url).override(Target.SIZE_ORIGINAL).dontTransform().submit().get()

fun AutoCompleteTextView.showDropdown(adapter: ArrayAdapter<String>?) {
    if (!TextUtils.isEmpty(this.text.toString())) {
        adapter?.filter?.filter(null)
    }
}

fun loadImageUrlPhotoView(photoView: PhotoView, imgUrl: String?) {
    imgUrl?.let {
        Glide.with(photoView.context).load(it)
            .apply(
                RequestOptions.placeholderOf(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            ).into(photoView)
    }
}

fun getGifFromUrl(gifDrawable: GifDrawable,context: Context):File{
    val dir = context.filesDir
    val gifFile = File(dir, "test.gif")
    val byteBuffer = gifDrawable.buffer
    val output = FileOutputStream(gifFile)
    val bytes = ByteArray(byteBuffer.capacity())
    (byteBuffer.duplicate().clear() as ByteBuffer).get(bytes)
    output.write(bytes, 0, bytes.size)
    output.close()
    return gifFile
}


