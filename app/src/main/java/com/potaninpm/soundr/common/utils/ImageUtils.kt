package com.potaninpm.soundr.common.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

object ImageUtils {

    fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun resizeBitmap(bitmap: Bitmap, maxWidth: Int = 500, maxHeight: Int = 500): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= maxWidth && height <= maxHeight) {
            return bitmap
        }

        val ratio = width.toFloat() / height.toFloat()

        val finalWidth: Int
        val finalHeight: Int

        if (width > height) {
            finalWidth = maxWidth
            finalHeight = (finalWidth / ratio).toInt()
        } else {
            finalHeight = maxHeight
            finalWidth = (finalHeight * ratio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true)
    }
} 