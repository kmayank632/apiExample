package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class UploadFileModel(
    val status: Boolean?,
    val message: String?,
    @SerializedName("data") val `data`: List<ImageData>?,
    val metaData: ImageMetaData
) : Parcelable
