package com.example.apipractice.datamodel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProfileModel(
    @SerializedName("data") val `data`: ProfileData?,
    val metaData: MetaData? = null
)