package com.example.apipractice.network

import androidx.lifecycle.LiveData
import com.example.apipractice.datamodel.LoginModel
import com.example.apipractice.datamodel.ProfileModel

/** Use For Login API Success Response */
interface AuthListener {
    fun onSuccess(loginResponse: LiveData<LoginModel>)
}
/** Use For Login API Success Response */
interface ProfileListener {
    fun onSuccess(loginResponse: LiveData<ProfileModel>)
}

