package com.example.apipractice.datamodel

import com.google.gson.annotations.SerializedName


data class ProfileModel(
    @SerializedName("data") val `data`: ProfileData?,
    val metaData: MetaData? = null
)