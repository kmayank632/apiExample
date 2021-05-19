package com.example.apipractice.datamodel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProfileData(
    @SerializedName("healthInsurance") val healthInsurance: ProfileHealthInsurance? = null,
    val medicalHistories: List<MedicalHistoryData>? = null,
    @SerializedName("existingAilments") val existingAilments: ProfileExistingAilments? = null,
    @SerializedName("firstName") val firstName: DataValue? = null,
    @SerializedName("lastName") val lastName: DataValue? = null,
    val firmName: DataValue? = null,
    val vleMainBusiness: DataValue? = null,
    val qualification: DataValue? = null,
    val type: String? = null,
    val dob: String? = null,
    val gender: String? = null,
    val email: String? = null,
    val alternateNumber: String? = null,
    val alternateNumber2: String? = null,
    val bloodGroup: String? = null,
    @SerializedName("_id") val _id: String? = null,
    @SerializedName("address") val address: ProfileAddress? = null,
    val number: String? = null,
    @SerializedName("documents") val documents: List<ProfileDocument>? = null,
    @SerializedName("pictures") val pictures: List<ProfileDocument>? = null,
    @SerializedName("__v") val __v: Double? = null,
    val medoplusId: String? = null,
    @SerializedName("_family") val _family: String? = null,
    val familyMemberMedoplusId: String? = null,
    val role: String? = null,
    val _draft: String? = null,
    val records: String? = null,
    val _vle: String? = null,
    val bankingDetails: BankDetails? = null,
    val _master: String? = null,
    val aggregateRating: Double? = null,
    val createdAt: String? = null,
    val distance: Double? = null,
    val location: List<Double>? = null,
    val status: String? = null
)