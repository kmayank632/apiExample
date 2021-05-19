package com.example.apipractice.view.fragment.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.apipractice.basemodel.BaseModel
import com.example.apipractice.network.AuthListner
import com.example.apipractice.repo.UserRepository
import com.google.gson.JsonObject

//TODO Write Proper Comments
//TODO Don't use static resources
class LoginVM : ViewModel() {
    /* UI Fields */
    val usernameField = ObservableField("PAP12MA00031")
    val passwordField = ObservableField("12345678")
    val isValidUsername = ObservableField(BaseModel(true))
    val isValidPassword = ObservableField(BaseModel(true))
    val visible = ObservableBoolean(false)

    var authListner: AuthListner? = null

    fun setLogin() {
        /* Check Username */
        if (usernameField.get()?.trim().isNullOrEmpty()) {
            /* Notify User */
            isValidUsername.set(
                BaseModel(
                    message = "Please enter valid id"
                )
            )
            isValidPassword.set(BaseModel(true))
            return
        }

        usernameField.set(usernameField.get()?.trim())

        /* Check Password */
        if (passwordField.get().isNullOrEmpty()) {
            /* Notify User */
            isValidPassword.set(
                BaseModel(
                    message = "Please enter valid password"
                )
            )
            isValidUsername.set(BaseModel(true))
            return
        }
        if (passwordField.get()?.length!! < 8) {
            isValidPassword.set(
                BaseModel(
                    message = "length > 8"
                )
            )
            isValidUsername.set(BaseModel(true))
            return
        }

        usernameField.set(usernameField.get()?.trim())

        val sessionJsonObject = JsonObject()
        sessionJsonObject.addProperty("device", "ANDROID")
        sessionJsonObject.addProperty("fcmToken", "fcmToken")
        sessionJsonObject.addProperty("deviceId", "deviceId")
        sessionJsonObject.addProperty("notificationsEnabled", true)

        val jsonObject = JsonObject()
        jsonObject.addProperty("username", usernameField.get()?.trim() ?: "")
        jsonObject.addProperty("password", passwordField.get() ?: "")
        jsonObject.add("session", sessionJsonObject)

        visible.set(true)
        val loginResponse = UserRepository().userLogin(jsonObject)
        authListner?.onSuccess(loginResponse)
        visible.set(false)
    }


}