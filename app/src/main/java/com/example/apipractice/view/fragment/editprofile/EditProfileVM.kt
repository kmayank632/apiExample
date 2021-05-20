package com.example.apipractice.view.fragment.editprofile

import android.content.ContentValues
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.apipractice.R
import com.example.apipractice.application.MyApplication
import com.example.apipractice.basemodel.BaseModel
import com.example.apipractice.basemodel.Constants
import com.example.apipractice.datamodel.ProfileData
import com.example.apipractice.network.ProfileListener
import com.example.apipractice.repo.UserRepository
import com.google.gson.JsonObject

class EditProfileVM : ViewModel() {

    /* Selected Sign Up Type */
    var signUpType = ObservableField(Constants.USER_TYPE.PATIENT)

    var profileData: ProfileData? = null

    val app = MyApplication.getApplication()


    /**
     * Signup Basic
     * */
    /* Ui Fields */
    val firstNameField = ObservableField("")
    val isValidFirstName = ObservableField(BaseModel(true, ""))
    val lastNameField = ObservableField("")
    val isValidLastName = ObservableField(BaseModel(true, ""))
    var radiochecked = ObservableField(R.id.radioMale)
    val alternatePhoneFirstNumberField = ObservableField("")
    val isValidAlternatePhoneNumberFirst = ObservableField(BaseModel(true, ""))
    val alternatePhoneSecondNumberField = ObservableField("")
    val isValidAlternatePhoneNumberSecond = ObservableField(BaseModel(true, ""))

    /* Data Members */
    var profileAuthListener: ProfileListener? = null


    fun updateProfileData() {
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
        Log.e(ContentValues.TAG, "response $jsonObject")

        val loginResponse = UserRepository().updateProfile(jsonObject)
        profileAuthListener?.onSuccess(loginResponse)
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