package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class ImageData(
    val bucket: String? = null,
    val path: String? = null,
    val type: String? = null,
    val preview: String?=null
) : Parcelable