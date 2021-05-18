package com.example.apipractice.datamodel

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("data") val `data`: LoginData?,
    @SerializedName("message") val message: String
)