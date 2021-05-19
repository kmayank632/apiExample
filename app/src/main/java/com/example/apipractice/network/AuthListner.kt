package com.example.apipractice.network

import androidx.lifecycle.LiveData
import com.example.apipractice.datamodel.LoginModel
import com.example.apipractice.datamodel.ProfileModel


/** Use for api success response */

/** Use for Login api success response */
interface AuthListner {
    fun onSuccess(loginResponse: LiveData<LoginModel>)
}
