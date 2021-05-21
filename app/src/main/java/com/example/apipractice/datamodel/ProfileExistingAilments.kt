package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class ProfileExistingAilments(
    @SerializedName("ailment") val ailment: List<ProfileAilment>?,
    val isCovidVaccined: Boolean?
) : Parcelable