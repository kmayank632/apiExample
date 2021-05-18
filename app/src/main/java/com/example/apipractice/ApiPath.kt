package com.example.apipractice

import com.example.apipractice.datamodel.LoginModel
import com.example.apipractice.datamodel.ProfileModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiPath {

    /** Service URL Path */
    companion object {
        const val PATH = "auth/"
    }

    /** Check Login Data */
    @POST("${PATH}login/medoplus")
    fun useLogin(@Body jsonObject: JsonObject): Call<LoginModel>

    /** Check Login Data */
    @GET("${PATH}patient/profile")
    fun getProfile(): Call<ProfileModel>
}