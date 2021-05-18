package com.example.apipractice.datamodel


data class LoginData(
    val userType: String? = "",
    val token: String? = "",
    val name: DataValue? = null
)