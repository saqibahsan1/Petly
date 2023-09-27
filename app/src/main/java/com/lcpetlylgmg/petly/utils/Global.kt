package com.lcpetlylgmg.petly.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore.Images
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


object Global {
    fun getCurrentUnixTimestamp(): Long {
        return System.currentTimeMillis() / 1000
    }

    fun generateRandomUUID(): String {
        return UUID.randomUUID().toString().uppercase(Locale.ROOT)
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }
    fun convertTimestampToReadableDate(timestamp: Long): String {
        val date = Date(timestamp * 1000) // Convert seconds to milliseconds
        val format = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        return format.format(date)
    }


    fun convertTimestampToReadableTime(timestamp: Long): String {
        val date = Date(timestamp * 1000) // Convert seconds to milliseconds
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return format.format(date)
    }

}