package com.ziss.storyapp.utils

import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.ziss.storyapp.R

fun ImageView.loadImage(url: Any?) {
    Glide.with(this.context).load(url).into(this)
        .onLoadFailed(AppCompatResources.getDrawable(this.context, R.drawable.ic_error_50))
}