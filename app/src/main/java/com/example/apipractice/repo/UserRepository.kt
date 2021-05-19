package com.example.apipractice.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.apipractice.datamodel.LoginModel
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

}