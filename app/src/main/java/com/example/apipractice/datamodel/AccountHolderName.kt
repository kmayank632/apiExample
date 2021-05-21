package com.example.apipractice.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class AccountHolderName(
    val en: String?,
    val hi: String?
) : Parcelable