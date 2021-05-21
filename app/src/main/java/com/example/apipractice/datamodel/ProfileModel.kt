package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class ProfileModel(
    val status: Boolean?,
    val message: String?,
    @SerializedName("data") val `data`: ProfileData?,
    val metaData: MetaData? = null
) : Parcelable