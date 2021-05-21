package com.example.apipractice.datamodel

import androidx.annotation.Keep


@Keep
data class BannerListModel(
    val `data`: List<BannerData>?
)