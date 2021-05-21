package com.example.apipractice.network

import com.example.apipractice.datamodel.BannerListModel
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

    companion object {
        const val PATH = "auth/"
        const val CMSPATH = "cms/"

    }
    /** Check Login Data */
    @POST("${PATH}login/medoplus")
    fun useLogin(@Body jsonObject: JsonObject): Call<LoginModel>

    /** Get Profile Data */
    @GET("${PATH}patient/profile")
    fun getProfile(): Call<ProfileModel>

    /** Update profile User Data */
    @PUT("${PATH}patient/profile")
    fun updateUserProfile(@Body jsonObject: JsonObject): Call<ProfileModel>

    /** Get Banner List Data */
    @POST("${CMSPATH}public/banner-listing")
    fun getBannerList(): Call<BannerListModel>



}