package com.example.apipractice.view.fragment.login

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apipractice.R
import com.example.apipractice.application.MyApplication
import com.example.apipractice.basemodel.BaseModel
import com.example.apipractice.network.AuthListener
import com.example.apipractice.repo.UserRepository
import com.example.apipractice.utills.StorePreferences
import com.example.apipractice.view.listener.ResourceProvider
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginVM : ViewModel() {
    /** UI Fields */
    val usernameField = ObservableField("PAP12MA00031")
    val passwordField = ObservableField("12345678")
    val isValidUsername = ObservableField(BaseModel(true))
    val isValidPassword = ObservableField(BaseModel(true))
    val visible = ObservableBoolean(false)

    /** Initialize ResourceProvider */
    val resourceProvider: ResourceProvider = ResourceProvider(MyApplication.getApplication())

    /** Initialize MyApplication */
    val app = MyApplication.getApplication()

    var storePreferences: StorePreferences = StorePreferences(app)

    var authListener: AuthListener? = null

    /** Login Button Click */
    fun setLogin()  {
            /** Check Username */
            if (usernameField.get()?.trim().isNullOrEmpty()) {
                /** Notify User */
                isValidUsername.set(
                    BaseModel(
                        message = resourceProvider.getString(R.string.please_enter_medoID)
                    )
                )
                /** Clear Error */
                isValidPassword.set(BaseModel(true))
                return
            }

            usernameField.set(usernameField.get()?.trim())

            /** Check Password */
            if (passwordField.get().isNullOrEmpty()) {
                /** Notify User */
                isValidPassword.set(
                    BaseModel(
                        message = resourceProvider.getString(R.string.please_enter_valid_password)
                    )
                )
                /** Clear Error */
                isValidUsername.set(BaseModel(true))
                return
            }
            /** Check Password Length*/
            if (passwordField.get()?.length!! < 8) {
                isValidPassword.set(
                    BaseModel(
                        message = resourceProvider.getString(R.string.length_greater_then_8)
                    )
                )
                /** Clear Error */
                isValidUsername.set(BaseModel(true))
                return
            }
            /** Trim UserName Field */
            usernameField.set(usernameField.get()?.trim())

            /** POST Request Body Parameters */
            val sessionJsonObject = JsonObject()
            sessionJsonObject.addProperty("device", "ANDROID")
            sessionJsonObject.addProperty("fcmToken", "fcmToken")
            sessionJsonObject.addProperty("deviceId", "deviceId")
            sessionJsonObject.addProperty("notificationsEnabled", true)

            val jsonObject = JsonObject()
            jsonObject.addProperty("username", usernameField.get()?.trim() ?: "")
            jsonObject.addProperty("password", passwordField.get() ?: "")
            jsonObject.add("session", sessionJsonObject)

            /** Show ProgressBar */
            visible.set(true)

            /** Get API Response */
            val loginResponse = UserRepository().userLogin(jsonObject)

            /** Pass Response to AuthListener */
            authListener?.onSuccess(loginResponse)

            /** Hide ProgressBar */
            visible.set(false)

    }


}