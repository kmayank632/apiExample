package com.example.apipractice.network

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

/**
 * Custom Upload Request Body
 *
 * @param file File which will be upload
 * @param contentType Type of content i.e. "image", "file" etc.
 * @param callback Used to get updates for upload file status
 * */
class UploadRequestBody(
    private val file: File,
    private val contentType: String,
    private val callback: UploadCallback,
    private val key: String? = null
) : RequestBody() {
    override fun contentType() = "$contentType/*".toMediaTypeOrNull()
    override fun contentLength() = file.length()

    /**
     * Note: If you are using "HttpLoggingInterceptor" for API Logging then
     * "writeTo" function will be called twice time. First Time For Internal Logging
     * and Second Time while Uploading
     * */
    override fun writeTo(sink: BufferedSink) {
        val length = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileInputStream = FileInputStream(file)
        var uploaded = 0L
        fileInputStream.use { inputStream ->
            var read: Int
            val handler = Handler(Looper.getMainLooper())
            while (inputStream.read(buffer).also { read = it } != -1) {
                handler.post(ProgressUpdater(uploaded, length))
                uploaded += read
                sink.write(buffer, 0, read)
            }
            handler.post(ProgressUpdater(length, length))
        }
    }

    interface UploadCallback {

        /**
         * Uploading File Status
         *
         * @param key To differentiate uploading files
         * @param percentage Uploaded Percentage status
         * */
        fun onProgressUpdate(key: String? = null, percentage: Int)
    }

    /**
     * Upload Multipart Status Callback
     *
     * @param uploaded current uploaded value
     * @param total total size of upload value
     * */
    inner class ProgressUpdater(
        private val uploaded: Long,
        private val total: Long
    ) : Runnable {
        override fun run() {
            callback.onProgressUpdate(
                key,
                (100 * uploaded / total).toInt()
            )
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }
}