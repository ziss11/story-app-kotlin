package com.ziss.storyapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: Any?) {
    Glide.with(this.context).load(url).into(this)
}