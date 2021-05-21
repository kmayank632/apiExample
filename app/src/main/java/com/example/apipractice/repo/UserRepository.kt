package com.example.apipractice.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.apipractice.application.MyApplication
import com.example.apipractice.datamodel.LoginModel
import com.example.apipractice.datamodel.ProfileModel
import com.example.apipractice.network.NetworkModule
import com.example.apipractice.utills.StorePreferences
import com.example.apipractice.view.listener.ResourceProvider
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    /** Login API Response*/
    fun userLogin(jsonObject: JsonObject): LiveData<LoginModel> {

        val loginResponse = MutableLiveData<LoginModel>()

        NetworkModule.retrofit.useLogin(jsonObject)
            .enqueue(object : Callback<LoginModel> {
                override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            loginResponse.postValue(response.body())
                            /** Store UseType in DataStore*/
                        }
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {

                }

            })

        return loginResponse
    }

    /** Update Profile API Response*/
    fun updateProfile(jsonObject: JsonObject): LiveData<ProfileModel> {

        val loginResponse = MutableLiveData<ProfileModel>()

        NetworkModule.retrofit.updateUserProfile(jsonObject)
            .enqueue(object : Callback<ProfileModel> {
                override fun onResponse(
                    call: Call<ProfileModel>,
                    response: Response<ProfileModel>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            loginResponse.postValue(response.body())

                        }

                    }
                }

                override fun onFailure(call: Call<ProfileModel>, t: Throwable) {

                }

            })

        return loginResponse
    }

    /** Get Profile API Response*/
    fun getProfile(): LiveData<ProfileModel> {
        val loginResponse = MutableLiveData<ProfileModel>()

        NetworkModule.retrofit.getProfile()
            .enqueue(object : Callback<ProfileModel> {
                override fun onResponse(

                    call: Call<ProfileModel>,
                    response: Response<ProfileModel>
                ) {
                    if (response.isSuccessful) {
                        loginResponse.postValue(response.body())
                    }
                }


                override fun onFailure(call: Call<ProfileModel>, t: Throwable) {

                }

            })
        return loginResponse

    }

}