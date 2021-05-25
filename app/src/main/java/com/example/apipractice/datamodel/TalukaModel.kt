package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class TalukaModel(
    val status: Boolean?,
    val message: String?,
    val `data`: DataX?
) : Parcelable