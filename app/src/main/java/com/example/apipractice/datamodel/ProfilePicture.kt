package com.example.apipractice.datamodel

import androidx.annotation.Keep

@Keep
data class ProfilePicture(
    val type: String?,
    val url: String?,
    val _id: String?
)