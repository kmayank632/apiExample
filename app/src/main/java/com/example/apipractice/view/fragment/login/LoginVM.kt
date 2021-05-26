package com.example.apipractice.view.fragment.login

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apipractice.R
import com.example.apipractice.application.MyApplication
import com.example.apipractice.basemodel.BaseModel
import com.example.apipractice.datamodel.LoginModel
import com.example.apipractice.network.NetworkModule
import com.example.apipractice.utills.StorePreferences
import com.example.apipractice.view.listener.ResourceProvider
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginVM : ViewModel() {

    /* UI Fields */
    val usernameField = ObservableField("PZZ0ZA00045")
    val passwordField = ObservableField("12345678")
    val isValidUsername = ObservableField(BaseModel(true))
    val isValidPassword = ObservableField(BaseModel(true))
    val visible = ObservableBoolean(false)

    /* MutableLiveData Variable to Store Response  */
    val loginResponse = MutableLiveData<LoginModel>()

    /* MutableLiveData Variable to Store Error  */
    var errorMessage = MutableLiveData<String?>()

    /* Initialize ResourceProvider */
    val resourceProvider: ResourceProvider = ResourceProvider(MyApplication.getApplication())

    /* Initialize MyApplication */
    val app = MyApplication.getApplication()

    /* Initialize the StorePreferences */
    var storePreferences: StorePreferences = StorePreferences(app)

    /* Login Button Click */
    fun setLogin() {
        /* Check Username */
        if (usernameField.get()?.trim().isNullOrEmpty()) {
            /* Notify User */
            isValidUsername.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_medoID)
                )
            )
            /* Clear Error */
            isValidPassword.set(BaseModel(true))
            return
        }

        usernameField.set(usernameField.get()?.trim())

        /* Check Password */
        if (passwordField.get().isNullOrEmpty()) {
            /* Notify User */
            isValidPassword.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_valid_password)
                )
            )
            /* Clear Error */
            isValidUsername.set(BaseModel(true))
            return
        }
        /* Check Password Length*/
        if (passwordField.get()?.length!! < 8) {
            isValidPassword.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.length_greater_then_8)
                )
            )
            /* Clear Error */
            isValidUsername.set(BaseModel(true))
            return
        }
        /* Trim UserName Field */
        usernameField.set(usernameField.get()?.trim())

        /* POST Request Body Parameters */
        val sessionJsonObject = JsonObject()
        sessionJsonObject.addProperty("device", "ANDROID")
        sessionJsonObject.addProperty("fcmToken", "fcmToken")
        sessionJsonObject.addProperty("deviceId", "deviceId")
        sessionJsonObject.addProperty("notificationsEnabled", true)

        val jsonObject = JsonObject()
        jsonObject.addProperty("username", usernameField.get()?.trim() ?: "")
        jsonObject.addProperty("password", passwordField.get() ?: "")
        jsonObject.add("session", sessionJsonObject)

        /* Show ProgressBar */
        visible.set(true)

        NetworkModule.retrofit.useLogin(jsonObject)
            .enqueue(object : Callback<LoginModel> {
                override fun onResponse(
                    call: Call<LoginModel>,
                    response: Response<LoginModel>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true && response.body() != null) {

                            /* Set Value*/
                            loginResponse.postValue(response.body())

                            /* Store UseType in DataStore*/
                            viewModelScope.launch {

                                response.body()?.data?.userType?.let { it1 ->
                                    storePreferences.storeValue(
                                        StorePreferences.User,
                                        it1
                                    )
                                }
                            }
                            viewModelScope.launch {
                                response.body()?.data?.token?.let { it1 ->
                                    storePreferences.storeValue(
                                        StorePreferences.Token,
                                        it1
                                    )
                                }
                            }

                            app.setToken(response.body()?.data?.token)

                            /* Set the message */
                            errorMessage.postValue(response.body()?.message)
                        } else {

                            /* Set the message */
                            errorMessage.postValue(response.body()?.message)
                        }
                    } else {
                        /* Set the message  */
                        errorMessage.postValue(response.message())
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {

                    /*Set the Failure message  */
                    errorMessage.postValue(t.cause.toString())
                }

            })


        /* Hide ProgressBar */
        visible.set(false)

    }
}

