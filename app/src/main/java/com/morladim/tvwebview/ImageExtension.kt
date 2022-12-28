package com.morladim.tvwebview

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions

fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .skipMemoryCache(true)
        .into(this)
        .clearOnDetach()
}

fun ImageView.loadImageWithCache(url: String) {
    Glide.with(this.context)
        .load(url)
        .skipMemoryCache(false)
        .into(this)
        .clearOnDetach()
}

fun ImageView.loadImageWithDefault(url: String, @DrawableRes defaultDrawableId: Int) {
    try {
        Glide.with(this.context)
            .load(url)
            .skipMemoryCache(true)
            .placeholder(defaultDrawableId)
            .into(this)
            .clearOnDetach()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Boolean?.default(default: Boolean): Boolean {
    return this ?: default
}

fun ImageView.loadImage(width: Int, height: Int, url: String?, decodeFormat: DecodeFormat) {
    val diskCacheStrategy = if (url?.startsWith("http").default(false)) {
        DiskCacheStrategy.DATA
    } else {
        DiskCacheStrategy.NONE
    }

    Glide.with(this.context)
        .asBitmap()
        .load(url)
        .format(decodeFormat)
        .priority(Priority.IMMEDIATE)
        .skipMemoryCache(true)
        .diskCacheStrategy(diskCacheStrategy)
        .transition(BitmapTransitionOptions.withCrossFade())
        .override(width, height)
        .fitCenter()
        .into(this)
        .clearOnDetach()
}

fun ImageView.loadImage(width: Int, height: Int, bitmap: ByteArray, decodeFormat: DecodeFormat) {
    Glide.with(this.context)
        .asBitmap()
        .load(bitmap)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .format(decodeFormat)
        .priority(Priority.IMMEDIATE)
        .skipMemoryCache(true)
        .transition(BitmapTransitionOptions.withCrossFade())
        .override(width, height)
        .fitCenter()
        .into(this)
        .clearOnDetach()
}

fun ImageView.getImageBitmap(): Bitmap? {
    return (this.drawable as BitmapDrawable).bitmap
}