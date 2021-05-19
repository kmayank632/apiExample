package com.example.apipractice.datamodel

import androidx.annotation.Keep

@Keep
data class LoginData(
    val userType: String? = "",
    val token: String? = "",
    val name: DataValue? = null
)