package com.example.apipractice.view.fragment.editprofile

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apipractice.R
import com.example.apipractice.application.MyApplication
import com.example.apipractice.basemodel.BaseModel
import com.example.apipractice.basemodel.Constants
import com.example.apipractice.datamodel.ProfileData
import com.example.apipractice.datamodel.ProfileModel
import com.example.apipractice.datamodel.TalukaModel
import com.example.apipractice.network.NetworkModule
import com.example.apipractice.view.listener.ResourceProvider
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileVM : ViewModel() {

    /* Selected Sign Up Type */
    var signUpType = ObservableField(SignUpType.CUSTOMER)

    /* Initialize ResourceProvider */
    val resourceProvider: ResourceProvider = ResourceProvider(MyApplication.getApplication())

    /** SignUp Type */
    enum class SignUpType {
        CUSTOMER
    }

    val addressFirstField = ObservableField("")
    val addressSecondField = ObservableField("")
    val isValidSecondAddress = ObservableField(BaseModel(true, ""))
    val isValidFirstAddress = ObservableField(BaseModel(true, ""))
    /* Data Members */
    var profileData: ProfileData? = null

    /* Initialize MyApplication variable */
    val app = MyApplication.getApplication()

    /* MutableLiveData Variable to Store Response  */
    val apiResponse = MutableLiveData<ProfileModel>()

    /* MutableLiveData Variable to Store Error  */
    var errorMessage = MutableLiveData<String?>()

    val isValidStateField= ObservableField(BaseModel(true, ""))
    val stateField = ObservableField("")
    val isValidDistrictField= ObservableField(BaseModel(true, ""))
    val districtField = ObservableField("")
    val talukaField = ObservableField("")
    val isValidTalukaField = ObservableField(BaseModel(true))
    val pinCodeField = ObservableField("")
    val isValidPinCode = ObservableField(BaseModel(true, ""))

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

    val talukaFieldList = mutableListOf<String>()
    private var alphaCodeField = ""
    private var stateCodeField = -1
    private var districtCodeField = -1
    var selectedTaluka = ""
    var latitude = 0.0
    var longitude = 0.0


    /** Listener to Detect Pin Code Changes */
    private var pinCodeChangeListener: Observable.OnPropertyChangedCallback =
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                pinCodeField.get()?.let { pinCode ->

                    if (pinCode.isNotEmpty()) {

                        /* Get Taluka Data */
                        getTaluka(pinCode)
                    } else {

                        /* Reset State/District/Block Data */
                        resetStateDistrictBlock()
                    }
                }
            }
        }

    /**
     * Reset State/District/Block Related Data
     * */
    private fun resetStateDistrictBlock() {
        talukaFieldList.clear()
        alphaCodeField = ""
        stateCodeField = -1
        districtCodeField = -1

        /* Ui Clear */
        stateField.set("")
        districtField.set("")
        talukaField.set("")
        talukaFieldList.clear()
    }

    init {
        /* Attach Listener */
        pinCodeField.addOnPropertyChangedCallback(pinCodeChangeListener)
    }

    /** Set Validation on EditText */
    fun checkValidation() {

        /* Check Username */
        if (firstNameField.get()?.trim().isNullOrEmpty()) {
            /* Notify User */
            isValidFirstName.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_valid_name)
                )
            )
            return
        }

        if (lastNameField.get()?.trim().isNullOrEmpty()) {
            /* Notify User */
            isValidLastName.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_valid_last_name)
                )
            )
            return
        }

        /* Check Address line 1 */
        if (addressFirstField.get()?.trim()
                .isNullOrEmpty() || (latitude == 0.0 && longitude == 0.0)
        ) {
            /* Notify User */
            isValidFirstAddress.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_valid)
                )
            )
            return
        }

        if (addressSecondField.get()?.trim().isNullOrEmpty()) {
            isValidSecondAddress.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_valid)
                )
            )
            return
        }

        if (pinCodeField.get()?.trim().isNullOrEmpty()) {
            isValidPinCode.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_valid)
                )
            )
            return
        }

        if (stateField.get()?.trim().isNullOrEmpty()) {
            isValidSecondAddress.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_valid)
                )
            )
            return
        }
        if (districtField.get()?.trim().isNullOrEmpty()) {
            isValidDistrictField.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_valid)
                )
            )
            return
        }
        if (talukaField.get()?.trim().isNullOrEmpty()) {
            isValidTalukaField.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_valid)
                )
            )
            return
        }
    }

    /** To Prevent From BackStack Issue */
    fun clearEditTextValidationErrors() {

        isValidFirstName.set(BaseModel(true))
        isValidLastName.set(BaseModel(true))
        isValidSecondAddress.set(BaseModel(true))
        isValidPinCode.set(BaseModel(true))
        isValidTalukaField.set(BaseModel(true))
        isValidDistrictField.set(BaseModel(true))
        isValidStateField.set(BaseModel(true))
        isValidFirstAddress.set(BaseModel(true))
        isValidAlternatePhoneNumberFirst.set(BaseModel(true))
        isValidAlternatePhoneNumberSecond.set(BaseModel(true))
    }

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

        data.address?.line1?.en?.let {
            addressFirstField.set(it.trim())
        }

        data.address?.line2?.en?.let {
            addressSecondField.set(it.trim())
        }

        data.address?.state?.en?.let {
            stateField.set(it)
        }

        data.address?.district?.en?.let {
            districtField.set(it)
        }

        data.address?.block?.en?.let {
            talukaField.set(it)
        }

        data.address?.zipcode?.let {
            if (it != pinCodeField.get()) {
                pinCodeField.set(it)
            }
        }

        data.address?.geo?.let {
            if (it.size == 2) {
                longitude = it[0]
                latitude = it[1]
            }
        }

        data.alternateNumber?.let {
            alternatePhoneFirstNumberField.set(it)
        }

        data.alternateNumber2?.let {
            alternatePhoneSecondNumberField.set(it)
        }


    }


    fun getTaluka(pinCode: String) {

        /* Reset State/District/Block Data */
        resetStateDistrictBlock()

        if (pinCode.length == 6) {

            NetworkModule.retrofit.getTaluka(pinCode)
                .enqueue(object : Callback<TalukaModel> {
                    override fun onResponse(

                        call: Call<TalukaModel>,
                        response: Response<TalukaModel>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == true) {
                                response.body()?.data?.let { data ->

                                    /* Set State Name */
                                    data.state?.let {
                                        stateField.set(it)
                                    }

                                    /* Set District Name */
                                    data.district?.let {
                                        districtField.set(it)
                                    }

                                    /* Set Block/Tehsil Data */
                                    data.taluka?.let {
                                        if (it.isNotEmpty()) {
                                            talukaField.set(it[0])
                                        }
                                        talukaFieldList.addAll(it)
                                    }

                                    /*Set the message  */
                                    errorMessage.postValue(response.body()?.message)
                                }
                            } else {
                                /*Set the message  */
                                errorMessage.postValue(response.body()?.message)
                            }
                        } else {
                            /*Set the message  */
                            errorMessage.postValue(response.message())
                        }
                    }


                    override fun onFailure(call: Call<TalukaModel>, t: Throwable) {

                        /*Set the Failure message  */
                        errorMessage.postValue(t.cause.toString())
                    }

                })

        }

    }
}