package com.example.apipractice.login

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.apipractice.ApiPath
import com.example.apipractice.datamodel.LoginModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginVM : ViewModel() {
    /* UI Fields */
    val usernameField = ObservableField("PAP12MA00031")
    val passwordField = ObservableField("12345678")
    val visible = ObservableField(View.GONE)
    val token = ObservableField("")
    val userType = ObservableField("")

    lateinit var apiInterface: ApiPath


    fun performLogin(): String {
        val sessionJsonObject = JsonObject()
        sessionJsonObject.addProperty("device", "ANDROID")
        sessionJsonObject.addProperty("fcmToken", "fcmToken")
        sessionJsonObject.addProperty("deviceId", "deviceId")
        sessionJsonObject.addProperty("notificationsEnabled", true)
        Log.e(TAG, "mssg1")

        val jsonObject = JsonObject()
        jsonObject.addProperty("username", usernameField.get()?.trim() ?: "")
        jsonObject.addProperty("password", passwordField.get() ?: "")
        jsonObject.add("session", sessionJsonObject)

        Log.e(TAG, "mssg $jsonObject")
        Log.e(TAG, "mssg2")

        val retrofit = Retrofit.Builder()
            .baseUrl("https://stage.api.medoplus.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        Log.e(TAG, "mssg3")

        apiInterface = retrofit.create(ApiPath::class.java)

        val userCall: Call<LoginModel> = apiInterface.useLogin(jsonObject)
        userCall.enqueue(object : Callback<LoginModel?> {

            override fun onResponse(call: Call<LoginModel?>, response: Response<LoginModel?>) {
                Log.e(TAG, "mssg4")

                token.set(response.body()?.data!!.token)

                if (response.body()?.data != null &&
                    !response.body()?.data!!.token?.trim().isNullOrEmpty()
                    && !response.body()?.data!!.userType?.trim().isNullOrEmpty()
                ) {
                    token.set(response.body()?.data!!.token)
                    userType.set(response.body()?.data!!.userType)



                } else {
                    Log.e(TAG, "Login Failed ${response.message()} ")

                }

            }

            override fun onFailure(call: Call<LoginModel?>, t: Throwable) {
                Log.e(TAG, "failure")
            }
        })

        visible.set(View.GONE)

        return token.get().toString()

    }

}