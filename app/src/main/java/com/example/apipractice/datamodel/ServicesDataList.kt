package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class ServicesDataList(
    val title: DataValue? = null,
    val description: DataValue? = null,
    val marketPriceKnown: Boolean? = null,
    val isDefault: Boolean? = null,
    val status: String? = null,
    @SerializedName("_id") val _id: String? = null,
    val type: String? = null,
    val pictures: List<ProfilePicture>? = null
) : Parcelable