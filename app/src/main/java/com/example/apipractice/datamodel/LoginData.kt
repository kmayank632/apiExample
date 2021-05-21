package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class LoginData(
    val userType: String? = "",
    val token: String? = "",
    val name: DataValue? = null
) : Parcelable