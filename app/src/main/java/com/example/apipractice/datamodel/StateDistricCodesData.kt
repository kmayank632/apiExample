package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class StateDistricCodesData(
    val alphacode: String?,
    val statecode: String?,
    val district: String?
) : Parcelable