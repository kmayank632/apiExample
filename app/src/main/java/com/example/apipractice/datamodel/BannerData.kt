package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class BannerData(
    val __v: Int?,
    val _id: String?,
    val createdAt: String?,
    val type: String?,
    val updatedAt: String?,
    val url: String?,
    val urls: List<Url>?
) : Parcelable

