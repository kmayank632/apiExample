package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class MedicalHistoryData(
    val __v: Int?,
    val _id: String?,
    val _patient: String?,
    val attachments: List<ProfileDocument>?,
    val createdAt: String?,
    val description: String?,
    val name: String?,
    val referenceDate: String?,
    val status: String?,
    val type: String?,
    val updatedAt: String?
) : Parcelable