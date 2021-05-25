package com.example.apipractice.datamodel

import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
@Keep
data class DataX(
    val district: String?,
    val state: String?,
    val taluka: List<String>?
) : Parcelable