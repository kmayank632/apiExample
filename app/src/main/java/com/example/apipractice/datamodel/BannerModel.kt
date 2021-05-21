package com.example.apipractice.datamodel

data class BannerModel(
    val `data`: List<Data?>?,
    val message: String?,
    val status: Boolean?
) {
    data class Data(
        val _id: String?,
        val createdAt: String?,
        val status: String?,
        val updatedAt: String?,
        val urls: List<Url?>?
    ) {
        data class Url(
            val en: String?,
            val hi: String?
        )
    }
}