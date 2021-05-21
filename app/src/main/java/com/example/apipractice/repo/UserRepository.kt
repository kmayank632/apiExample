package com.example.apipractice.repo

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.apipractice.datamodel.BannerListModel
import com.example.apipractice.datamodel.LoginModel
import com.example.apipractice.datamodel.ProfileModel
import com.example.apipractice.network.MyApi
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    fun userLogin(jsonObject: JsonObject): LiveData<LoginModel> {

        val loginResponse = MutableLiveData<LoginModel>()
        val responsee = MutableLiveData<String>()

        MyApi().useLogin(jsonObject)
            .enqueue(object : Callback<LoginModel> {
                override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                    if (response.isSuccessful) {
                        if (response.body() != null ) {

                            //TODO Use postValue() Function
                            loginResponse.value = response.body()
                        }
                    } else {
                        responsee.value = response.errorBody().toString()
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    responsee.value = t.message.toString()

                }

            })

        return loginResponse
    }

    fun updateProfile(jsonObject: JsonObject): LiveData<ProfileModel> {

        val loginResponse = MutableLiveData<ProfileModel>()

        MyApi().updateUserProfile(jsonObject)
            .enqueue(object : Callback<ProfileModel> {
                override fun onResponse(call: Call<ProfileModel>, response: Response<ProfileModel>) {
                    if (response.isSuccessful) {
                        if (response.body() != null ) {

                             loginResponse.postValue(response.body())

                        }

                    }
                }

                override fun onFailure(call: Call<ProfileModel>, t: Throwable) {

                }

            })

        return loginResponse
    }

    fun getProfile(): LiveData<ProfileModel> {
        val loginResponse = MutableLiveData<ProfileModel>()

        MyApi().getProfile()
            .enqueue(object : Callback<ProfileModel> {
                override fun onResponse(

                    call: Call<ProfileModel>,
                    response: Response<ProfileModel>
                ) {
                    if (response.isSuccessful) {
                        loginResponse.value = response.body()
                    }
                }


                override fun onFailure(call: Call<ProfileModel>, t: Throwable) {

                }

            })
        return loginResponse

    }


    fun getBanner(): LiveData<BannerListModel> {
        val loginResponse = MutableLiveData<BannerListModel>()

        MyApi().getBannerList()
            .enqueue(object : Callback<BannerListModel> {
                override fun onResponse(

                    call: Call<BannerListModel>,
                    response: Response<BannerListModel>
                ) {
                    if (response.isSuccessful) {
                        loginResponse.value = response.body()
                    }
                }


                override fun onFailure(call: Call<BannerListModel>, t: Throwable) {

                }

            })
        return loginResponse

    }

}