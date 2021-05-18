package com.example.apipractice.profile

import android.content.ContentValues
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.apipractice.ApiPath
import com.example.apipractice.MyIntercepter
import com.example.apipractice.datamodel.ProfileData
import com.example.apipractice.datamodel.ProfileModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProfileVM : ViewModel() {

    /* Data Members */
    var profileData: ProfileData? = null

    /* Ui Fields */
    val profilePictureField = ObservableField("")
    val medoPlusIdField = ObservableField("")
    val firstNameField = ObservableField("")
    val lastNameField = ObservableField("")
    val dobField = ObservableField("")
    val addressFirstField = ObservableField("")
    val phoneNumberField = ObservableField("")
    val bloodGroup = ObservableField("")

    /* Health Profile */
    val healthIssuesField = ObservableField("")
    val healthCoverageField = ObservableField(false)
    val healthProviderField = ObservableField("")
    val healthPolicyNumberField = ObservableField("")
    lateinit var apiInterface: ApiPath


    fun getProfileData() {
        val client = OkHttpClient.Builder().apply {
            addInterceptor(MyIntercepter())
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://stage.api.medoplus.org/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiInterface = retrofit.create(
            ApiPath::
            class.java
        )

        val userCall: Call<ProfileModel> = apiInterface.getProfile()
        userCall.enqueue(
            object : Callback<ProfileModel?> {
                override fun onResponse(
                    call: Call<ProfileModel?>,
                    response: Response<ProfileModel?>
                ) {
                    Log.e(ContentValues.TAG, "Login  ${response.body()}  ")

                }

                override fun onFailure(call: Call<ProfileModel?>, t: Throwable) {
                }

            })

    }

}