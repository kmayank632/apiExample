package com.example.apipractice.profile

import android.content.ContentValues
import android.util.Log
import android.view.PixelCopy.request
import androidx.core.view.DragAndDropPermissionsCompat.request
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.apipractice.ApiPath
import com.example.apipractice.datamodel.LoginModel
import com.example.apipractice.datamodel.ProfileData
import com.example.apipractice.datamodel.ProfileModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.file.attribute.AclEntry.newBuilder
import java.util.concurrent.TimeUnit


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

//        var request = Interceptor.Chain.r
//        val requestBuilder = Interceptor.Chain.request.newBuilder()
//        requestBuilder.addHeader("Accept:", "application/json")
//
//            requestBuilder.addHeader(
//                "Authorization"
//                "Bearer ${"jbjjhb"}"
//            )
//        }
//
//        val okHttpClient = OkHttpClient.Builder()
//            .connectTimeout(100, TimeUnit.SECONDS)
//            .readTimeout(100, TimeUnit.SECONDS)
//            .addInterceptor()


        val retrofit = Retrofit.Builder()
            .baseUrl("https://stage.api.medoplus.org/")
//            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiInterface= retrofit.create(ApiPath::class.java)

        val userCall: Call<ProfileModel> = apiInterface.getProfile()
        userCall.enqueue(object : Callback<ProfileModel?> {
            override fun onResponse(call: Call<ProfileModel?>, response: Response<ProfileModel?>) {
                Log.e(ContentValues.TAG, "Login  ${response.body()}  ")

            }

            override fun onFailure(call: Call<ProfileModel?>, t: Throwable) {
            }

        })

    }

}