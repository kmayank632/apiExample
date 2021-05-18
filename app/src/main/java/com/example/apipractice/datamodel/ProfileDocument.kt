package com.example.apipractice.datamodel

import com.google.gson.annotations.SerializedName

data class ProfileDocument(
    @SerializedName("_id") val _id: String? = null,
    val type: String? = null,
    val preview: String? = null,
    val url: String? = null
)
