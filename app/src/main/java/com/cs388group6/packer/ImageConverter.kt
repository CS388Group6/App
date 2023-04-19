package com.cs388group6.packer

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

class ImageConverter {
    companion object {
        fun bitmapToString(bitmap: Bitmap): String {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
    }
}

var converter = ImageConverter.Companion