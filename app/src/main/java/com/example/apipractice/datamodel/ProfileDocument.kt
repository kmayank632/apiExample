package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class ProfileDocument(
    @SerializedName("_id") val _id: String? = null,
    val type: String? = null,
    val preview: String? = null,
    val url: String? = null
) : Parcelable
