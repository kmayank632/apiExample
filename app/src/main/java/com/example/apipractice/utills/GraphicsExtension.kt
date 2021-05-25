package com.example.apipractice.utills

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface

/**
 * Drawable Extension Function
 * */
const val TAG = "GraphicsExtension"

/**
 * Get Bitmap from Drawable
 * */
fun Drawable.getBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(
        this.intrinsicWidth,
        this.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return bitmap
}

/**
 * Rotate the Bitmap if required
 * */
fun rotateImageIfRequired(context: Context, bitmapImage: Bitmap, selectedImage: Uri): Bitmap? {
    try {
        var secondTry = true
        if (selectedImage.scheme == "content") {
            secondTry = try {
                val projection = arrayOf(MediaStore.Images.ImageColumns.ORIENTATION)
                val c = context.contentResolver.query(
                    selectedImage, projection, null,
                    null, null
                )
                if (c?.moveToFirst() == true) {
                    val rotation = c.getInt(0)
                    c.close()
                    return rotateImage(bitmapImage, rotation)
                }
                return bitmapImage
            } catch (e: Exception) {
                logger(TAG, "rotateImageIfRequired - secondTry: ${e.message}", LogType.ERROR)
                true
            }
        }
        if (secondTry) {
            return modifyOrientation(bitmapImage, selectedImage.path ?: "")
        }
    } catch (e: Exception) {
        logger(TAG, "rotateImageIfRequired: ${e.message}", LogType.ERROR)
        return bitmapImage
    }
    return bitmapImage
}

/**
 * Change Orientation of the Bitmap
 * */
fun modifyOrientation(bitmapImage: Bitmap, image_absolute_path: String): Bitmap? {
    val ei = ExifInterface(image_absolute_path)
    return when (val orientation = ei.rotationDegrees) {

        /* Dependency Add: implementation "androidx.exifinterface:exifinterface:X.X.X" */
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmapImage, 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmapImage, 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmapImage, 270)
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(
            bitmapImage,
            horizontal = true,
            vertical = false
        )
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(
            bitmapImage,
            horizontal = false,
            vertical = true
        )
        else -> rotateImage(bitmapImage, orientation)
    }
}

/**
 * Flip Bitmap
 * */
fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap? {
    return try {
        val matrix = Matrix()
        matrix.preScale(if (horizontal) -1f else 1.toFloat(), if (vertical) -1f else 1.toFloat())
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } catch (e: Exception) {
        logger(TAG, "flip: ${e.message}", LogType.ERROR)
        bitmap
    }
}

/**
 * Rotate Bitmap with Rotation Degree Value
 * */
fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
    return try {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    } catch (e: Exception) {
        logger(TAG, "rotateImage: ${e.message}", LogType.ERROR)
        img
    }
}

