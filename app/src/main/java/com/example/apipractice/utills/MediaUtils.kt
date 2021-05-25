package com.example.apipractice.utills

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

/**
 * Class to Handle Operation for Media (Image etc) Files
 * */
class MediaUtils {

    /* File Size */
    interface FILE {
        companion object {
            const val IMAGE_SIZE = 1000000 //1000 kb
            const val IMAGE_DESIRED_HEIGHT = 1280f
            const val IMAGE_DESIRED_WIDTH = 720f
            const val EXTENSION = ".jpg"
        }
    }

    /**
     * Generate Dummy Image File for Reference
     * */
    fun getPictureFile(mContext: Context): File {
        val pictureFile = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
            Date()
        )
        val storageDir = File("${mContext.getExternalFilesDir(Environment.DIRECTORY_DCIM)}")

        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        val file = File(storageDir, pictureFile + FILE.EXTENSION)
        if (file.exists())
            file.delete()
        //file.createNewFile()
        return file
    }

    /**
     * Get Bitmap from URI
     * */
    private fun getThumbnail(mContext: Context, uri: Uri?): Bitmap? {
        var bitmap: Bitmap? = null

        if (uri == null) {
            return null
        }

        try {
            var input = mContext.contentResolver.openInputStream(uri)
            val onlyBoundsOptions = BitmapFactory.Options()
            onlyBoundsOptions.inJustDecodeBounds = true
            //  onlyBoundsOptions.inDither = true;  //optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
            input?.close()
            if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1) return null
            val originalSize =
                if (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth)
                    onlyBoundsOptions.outHeight
                else onlyBoundsOptions.outWidth
            val ratio = if (originalSize > 180) (originalSize.toFloat() / 180).toDouble() else 1.0
            //  double ratio = 1.0;
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio)
            //  bitmapOptions.inDither = true; //optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
            input = mContext.contentResolver.openInputStream(uri)
            bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
            input?.close()
        } catch (e: Exception) {
            logger(javaClass.simpleName, "rotateImage: ${e.message}", LogType.ERROR)
        }
        return bitmap
    }

    /**
     * Generate Image and get Path
     * */
    suspend fun compressImageFile(
        context: Context,
        path: String,
        shouldOverride: Boolean = true,
        uri: Uri
    ): String {
        return withContext(Dispatchers.IO) {
            var scaledBitmap: Bitmap? = null

            try {
                val (hgt, wdt) = getImageHgtWdt(
                    context, uri, FILE.IMAGE_DESIRED_HEIGHT,
                    FILE.IMAGE_DESIRED_WIDTH
                )
                try {
                    val bm = getBitmapFromUri(context, uri)
                    logger(
                        this@MediaUtils.javaClass.simpleName,
                        "compressImageFile: getBitmapFromUri: original bitmap height${bm?.height} width${bm?.width}"
                    )
                    logger(
                        this@MediaUtils.javaClass.simpleName,
                        "compressImageFile: getBitmapFromUri: Dynamic height$hgt width$wdt"
                    )
                } catch (e: Exception) {
                    logger(
                        this@MediaUtils.javaClass.simpleName,
                        "compressImageFile: getBitmapFromUri: ${e.message}",
                        LogType.ERROR
                    )
                }

                // Part 1: Decode image
                val unscaledBitmap = decodeFileFromUri(context, uri, wdt, hgt, ScalingLogic.FIT)

                if (unscaledBitmap != null) {

                    scaledBitmap = if (!(unscaledBitmap.width <= FILE.IMAGE_DESIRED_WIDTH
                                && unscaledBitmap.height <= FILE.IMAGE_DESIRED_HEIGHT)
                    ) {

                        // Part 2: Scale image
                        createScaledBitmap(unscaledBitmap, wdt, hgt, ScalingLogic.FIT)
                    } else {
                        unscaledBitmap
                    }
                }

                if (scaledBitmap != null) {
                    try {
                        scaledBitmap = modifyOrientation(scaledBitmap, path)
                    } catch (e: Exception) {
                        logger(
                            this@MediaUtils.javaClass.simpleName,
                            "compressImageFile: modifyOrientation - ${e.message}",
                            LogType.ERROR
                        )
                    }
                }

                scaledBitmap?.apply {
                    rotateImageIfRequired(
                        context,
                        this, Uri.fromFile(
                            File(path)
                        )
                    )
                }

                // Store to tmp file
                val tmpFile = getFileImage(context, scaledBitmap)

                var compressedPath = ""
                if (tmpFile != null && tmpFile.exists() && tmpFile.length() > 0) {
                    compressedPath = tmpFile.absolutePath
                    if (shouldOverride) {
                        val srcFile = File(path)
                        val result = tmpFile.copyTo(srcFile, true)
                        logger(
                            this@MediaUtils.javaClass.simpleName,
                            "compressImageFile: copyTo: copied file ${result.absolutePath}"
                        )
                        logger(
                            this@MediaUtils.javaClass.simpleName,
                            "compressImageFile: copyTo: Delete temp file ${tmpFile.delete()}"
                        )
                    }
                }

                scaledBitmap?.recycle()

                return@withContext if (shouldOverride) path else compressedPath
            } catch (e: Throwable) {
                e.printStackTrace()
            }

            return@withContext ""
        }
    }

    /**
     * Get Image from Bitmap
     * */
    fun getFileFromBitmap(context: Context, myBitmap: Bitmap): String {
        val tmpFile = getFileImage(context, myBitmap)
        if (tmpFile != null && tmpFile.exists() && tmpFile.length() > 0) {
            return tmpFile.absolutePath
        }
        return ""
    }

    /**
     * Get File from Bitmap
     * */
    private fun getFileImage(context: Context, myBitmap: Bitmap?): File? {
        var file: File? = null
        try {
            val output: OutputStream
            val fileFolder = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

            fileFolder?.let { folder ->

                if (!folder.exists()) {
                    folder.mkdirs()
                }

                file = File(
                    folder.absolutePath,
                    "${System.currentTimeMillis()}${FILE.EXTENSION}"
                )
                try {
                    output = FileOutputStream(file)
                    myBitmap?.compress(Bitmap.CompressFormat.JPEG, file.let {
                        if (it != null) {
                            getImageQualityPercent(it)
                        } else {
                            100
                        }
                    }, output)
                    output.flush()
                    output.close()
                } catch (e: java.lang.Exception) {
                    logger(
                        javaClass.simpleName,
                        "getFileName: BitmapCompressFormat: ${e.message}",
                        LogType.ERROR
                    )
                }
            }
        } catch (e: java.lang.Exception) {
            logger(javaClass.simpleName, "getFileName: ${e.message}", LogType.ERROR)
        }
        return file
    }

    /**
     * Get URI from File Location
     * */
    private fun getUri(context: Context, file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // first add provider in AndroidManifest.xml and create an xml file in xml file folder
            // for new versions or greater than marshmallow we need to use fileProvide
            try {
                FileProvider.getUriForFile(
                    context, context.applicationContext.packageName + ".provider",
                    file
                )
            } catch (e: java.lang.Exception) {
                logger(javaClass.simpleName, "getUri: ${e.message}", LogType.ERROR)
                Uri.fromFile(file)
            }
        } else {
            // Below 23 Version Api get Uri directly
            Uri.fromFile(file)
        }
    }

    /**
     * Get Bitmap from URI
     * */
    fun getBitmapFromUri(
        context: Context,
        uri: Uri,
        options: BitmapFactory.Options? = null
    ): Bitmap? {
        return try {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
            val fileDescriptor = parcelFileDescriptor?.fileDescriptor
            val image: Bitmap? = if (options != null)
                BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
            else
                BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor?.close()
            image
        } catch (e: Exception) {
            logger(
                this@MediaUtils.javaClass.simpleName,
                "getBitmapFromUri: ${e.message}"
            )
            null
        }

    }

    /**
     * Calculate Ratio Size for Bitmaps
     * */
    private fun getPowerOfTwoForSampleRatio(ratio: Double): Int {
        val k = Integer.highestOneBit(floor(ratio).toInt())
        return if (k == 0) 1 else k
    }
}