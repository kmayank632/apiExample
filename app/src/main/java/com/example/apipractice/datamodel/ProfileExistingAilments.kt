package com.example.apipractice.datamodel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProfileExistingAilments(
    @SerializedName("ailment") val ailment: List<ProfileAilment>?,
    val isCovidVaccined: Boolean?
)