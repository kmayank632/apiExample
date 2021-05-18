package com.example.apipractice.datamodel

import com.google.gson.annotations.SerializedName


data class ProfileHealthInsurance(
    val isActive: Boolean?,
    val companyName: String?,
    val policyNumber: String?,
    @SerializedName("attachments") val attachments: List<ProfileDocument>?
)