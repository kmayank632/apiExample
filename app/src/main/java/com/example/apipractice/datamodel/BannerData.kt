package com.example.apipractice.datamodel

import androidx.annotation.Keep

@Keep
data class BannerData(
    val __v: Int?,
    val _id: String?,
    val createdAt: String?,
    val type: String?,
    val updatedAt: String?,
    val url: String?,
    val urls: List<Url>?
) {
    data class Url(
        val en : String?,
        val hi  :  String?
    )
}