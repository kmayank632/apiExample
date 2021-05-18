package com.example.apipractice.datamodel

import com.google.gson.annotations.SerializedName

data class ProfileExistingAilments(
    @SerializedName("ailment") val ailment: List<ProfileAilment>?,
    val isCovidVaccined: Boolean?
)