package com.example.apipractice.network

import com.example.apipractice.datamodel.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

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
    fun getProfile(): Call<CustomerProfileModel>

    /** Update profile User Data */
    @PUT("${PATH}patient/profile")
    fun updateUserProfile(@Body jsonObject: JsonObject): Call<UpdateCustomerProfile>

    /** Get Banner List Data */
    @POST("${CMSPATH}public/banner-listing")
    fun getBannerList(): Call<BannerListModel>

    /** Get Block/Tehsil From PinCode */
    @GET("${PATH}open/address-autofill")
    fun getTaluka(@Query("pin") pinCode: String): Call<TalukaModel>

    /** Upload File to Server */
    @Multipart
    @POST("media/patient/files")
    fun uploadCustomerImage(
        @Part("thumbnail") thumbnail: RequestBody,
        @Part multipart: MultipartBody.Part
    ): Call<UploadFileModel>

    /** Get State Distric Codes */
    @GET("${PATH}open/address-autofill/codes")
    fun getStateDistricCodes(
        @Query("state") state: String,
        @Query("district") district: String
    ): Call<StateDistricCodesModel>


}