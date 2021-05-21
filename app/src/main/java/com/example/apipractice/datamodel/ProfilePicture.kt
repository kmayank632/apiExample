package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class ProfilePicture(
    val type: String?,
    val url: String?,
    val _id: String?
) : Parcelable