package com.example.apipractice.datamodel

import com.google.gson.annotations.SerializedName


data class ProfileAilment(
    @SerializedName("name") val name: DataValue?,
    @SerializedName("_id") val _id: String?,
    @SerializedName("pictures") val pictures: List<ProfilePicture>?,
    val id: String
)