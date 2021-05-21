package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class ProfileAilment(
    @SerializedName("name") val name: DataValue?,
    @SerializedName("_id") val _id: String?,
    @SerializedName("pictures") val pictures: List<ProfilePicture>?,
    val id: String
) : Parcelable