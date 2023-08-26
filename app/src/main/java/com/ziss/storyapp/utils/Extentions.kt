package com.ziss.storyapp.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun ImageView.loadImage(url: Any?) {
    Glide.with(this.context).load(url).into(this)
}

fun Uri.toFile(context: Context): File {
    val contentResolver = context.contentResolver
    val file = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(this) as InputStream
    val outputStream = FileOutputStream(file)

    val buf = ByteArray(1024)
    var len: Int

    while (inputStream.read(buf).also { len = it } > 0) {
        outputStream.write(buf, 0, len)
    }

    outputStream.close()
    inputStream.close()

    return file
}

