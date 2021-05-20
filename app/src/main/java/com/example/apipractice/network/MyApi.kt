package com.example.apipractice.network

import com.example.apipractice.datamodel.LoginModel
import com.example.apipractice.datamodel.ProfileModel
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface MyApi {

    /** Check Login Data */
    @POST("${PATH}login/medoplus")
    fun useLogin(@Body jsonObject: JsonObject): Call<LoginModel>

    /** Check Login Data */
    @GET("${PATH}patient/profile")
    fun getProfile(): Call<ProfileModel>

    /** Update profile User Data */
    @PUT("${PATH}patient/profile")
    fun updateUserProfile(@Body jsonObject: JsonObject): Call<ProfileModel>

    companion object {
        const val PATH = "auth/"

        //TODO Move to another package
        private val client = OkHttpClient.Builder().apply {
            addInterceptor(MyIntercepter())
        }.build()

        operator fun invoke(): MyApi {
            return Retrofit.Builder()
                .baseUrl("https://stage.api.medoplus.org/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}