package com.example.apipractice.datamodel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProfileHealthInsurance(
    val isActive: Boolean?,
    val companyName: String?,
    val policyNumber: String?,
    @SerializedName("attachments") val attachments: List<ProfileDocument>?
)