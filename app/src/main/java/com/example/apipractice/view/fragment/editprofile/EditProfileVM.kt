package com.example.apipractice.view.fragment.editprofile

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apipractice.R
import com.example.apipractice.application.MyApplication
import com.example.apipractice.basemodel.BaseModel
import com.example.apipractice.basemodel.Constants
import com.example.apipractice.datamodel.ProfileData
import com.example.apipractice.datamodel.ProfileModel
import com.example.apipractice.network.NetworkModule
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileVM : ViewModel() {

    /* Data Members */
    var profileData: ProfileData? = null

    /* Initialize MyApplication variable */
    val app = MyApplication.getApplication()

    /* MutableLiveData Variable to Store Response  */
    val apiResponse = MutableLiveData<ProfileModel>()

    /* MutableLiveData Variable to Store Error  */
    var errorMessage = MutableLiveData<String?>()

    /**
     * Edit Basic Details Field
     * */
    /** Ui Fields */
    val firstNameField = ObservableField("")
    val isValidFirstName = ObservableField(BaseModel(true, ""))
    val lastNameField = ObservableField("")
    val isValidLastName = ObservableField(BaseModel(true, ""))
    var radiochecked = ObservableField(R.id.radioMale)
    val alternatePhoneFirstNumberField = ObservableField("")
    val isValidAlternatePhoneNumberFirst = ObservableField(BaseModel(true, ""))
    val alternatePhoneSecondNumberField = ObservableField("")
    val isValidAlternatePhoneNumberSecond = ObservableField(BaseModel(true, ""))

    /** On Update Button Click */
    fun updateProfileData() {

        /** PUT Request Body Parameters */
        val jsonObject = JsonObject()
        val firstNameJsonObject = JsonObject()
        firstNameJsonObject.addProperty(
            "en",
            firstNameField.get()?.trim() ?: ""
        )
        firstNameJsonObject.addProperty("hi", firstNameField.get()?.trim() ?: "")
        jsonObject.add(
            "firstName",
            firstNameJsonObject
        )

        val lastNameJsonObject = JsonObject()
        lastNameJsonObject.addProperty(
            "en",
            lastNameField.get()?.trim() ?: ""
        )
        lastNameJsonObject.addProperty("hi", lastNameField.get()?.trim() ?: "")
        jsonObject.add(
            "lastName",
            lastNameJsonObject
        )
        jsonObject.addProperty(
            "gender",
            when (radiochecked.get()) {
                R.id.radioFemale -> Constants.GENDER.FEMALE
                R.id.radioTransgender -> Constants.GENDER.TRANSGENDER
                else -> Constants.GENDER.MALE
            }
        )
        if (!alternatePhoneFirstNumberField.get().isNullOrEmpty()) {
            jsonObject.addProperty(
                "alternateNumber",
                alternatePhoneFirstNumberField.get()
            )
        }

        if (!alternatePhoneSecondNumberField.get().isNullOrEmpty()) {
            jsonObject.addProperty(
                "alternateNumber2",
                alternatePhoneSecondNumberField.get()
            )
        }

        /** Call API */
        NetworkModule.retrofit.updateUserProfile(jsonObject)
            .enqueue(object : Callback<ProfileModel> {
                override fun onResponse(
                    call: Call<ProfileModel>,
                    response: Response<ProfileModel>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null && response.body()?.status == true) {

                            /* Set Value*/
                            apiResponse.postValue(response.body())

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

                override fun onFailure(call: Call<ProfileModel>, t: Throwable) {

                    /* Set the message  */
                    errorMessage.postValue(t.cause.toString())
                }

            })

    }

    /**
     * Set Profile Data
     * */
    fun setUiData(data: ProfileData) {

        profileData = data

        /* Update Gender Selection */
        when (data.gender) {

            Constants.GENDER.FEMALE -> {
                radiochecked.set(R.id.radioFemale)
            }

            Constants.GENDER.TRANSGENDER -> {
                radiochecked.set(R.id.radioTransgender)
            }

            else -> {
                radiochecked.set(R.id.radioMale)
            }
        }

        /* Reset Fields Data */
        firstNameField.set("")
        lastNameField.set("")
        alternatePhoneFirstNumberField.set("")
        alternatePhoneSecondNumberField.set("")
        data.firstName?.en?.let {
            firstNameField.set(it.trim())
        }

        data.lastName?.en?.let {
            lastNameField.set(it.trim())
        }

        data.alternateNumber?.let {
            alternatePhoneFirstNumberField.set(it)
        }

        data.alternateNumber2?.let {
            alternatePhoneSecondNumberField.set(it)
        }


    }
}