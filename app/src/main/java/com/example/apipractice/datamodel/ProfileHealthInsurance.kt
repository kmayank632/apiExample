package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class ProfileHealthInsurance(
    val isActive: Boolean?,
    val companyName: String?,
    val policyNumber: String?,
    @SerializedName("attachments") val attachments: List<ProfileDocument>?
) : Parcelable