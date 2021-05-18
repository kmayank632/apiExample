package com.example.apipractice.network

import androidx.lifecycle.LiveData
import com.example.apipractice.datamodel.LoginModel
import com.example.apipractice.datamodel.ProfileModel

interface AuthListner {
    fun onSuccess(loginResponse: LiveData<LoginModel>)
}

interface ProfileListner{
    fun onSuccess(loginResponse: LiveData<ProfileModel>)
}