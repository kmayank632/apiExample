package com.example.apipractice.view.fragment.editprofile

import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apipractice.R
import com.example.apipractice.application.MyApplication
import com.example.apipractice.basemodel.BaseModel
import com.example.apipractice.basemodel.Constants
import com.example.apipractice.datamodel.CustomerProfileModel
import com.example.apipractice.datamodel.StateDistricCodesModel
import com.example.apipractice.datamodel.TalukaModel
import com.example.apipractice.datamodel.UpdateCustomerProfile
import com.example.apipractice.network.NetworkModule
import com.example.apipractice.utills.TAG
import com.example.apipractice.view.listener.ResourceProvider
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EditProfileVM : ViewModel() {

    /* Selected Sign Up Type */
    var signUpType = ObservableField(SignUpType.CUSTOMER)

    /*IsValid UI fields */
    val isValidFirstName = ObservableField(BaseModel(true))
    val isValidSecondAddress = ObservableField(BaseModel(true))
    val isValidFirstAddress = ObservableField("")
    val isValidLastName = ObservableField(BaseModel(true))
    val isValidPinCode = ObservableField(BaseModel(true))
    val isValidAlternatePhoneNumberFirst = ObservableField(BaseModel(true))
    val isValidAlternatePhoneNumberSecond = ObservableField(BaseModel(true))

    /* Initialize ResourceProvider */
    val resourceProvider: ResourceProvider = ResourceProvider(MyApplication.getApplication())

    /*for Capitalize First Letter*/
    fun String.capitalizeFirstLetter() = split(" ").map {
        it.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }.joinToString(" ")


    /** SignUp Type */
    enum class SignUpType {
        CUSTOMER
    }

    val addressFirstField = ObservableField("")
    val addressSecondField = ObservableField("")
    val visibleLoader = ObservableBoolean(false)

    /* Data Members */
    var profileData: CustomerProfileModel.Data? = null

    /* Initialize MyApplication variable */
    val app = MyApplication.getApplication()

    /* MutableLiveData Variable to Store Response  */
    val apiResponse = MutableLiveData<UpdateCustomerProfile>()

    /* MutableLiveData Variable to Store Error  */
    var errorMessage = MutableLiveData<String?>()

    val stateField = ObservableField("")
    val districtField = ObservableField("")
    val talukaField = ObservableField("")
    val pinCodeField = ObservableField("")

    /**
     * Edit Basic Details Field
     * */
    /** Ui Fields */
    val firstNameField = ObservableField("")
    val lastNameField = ObservableField("")
    var radiochecked = ObservableField(R.id.radioMale)
    val alternatePhoneFirstNumberField = ObservableField("")
    val alternatePhoneSecondNumberField = ObservableField("")


    val talukaFieldList = mutableListOf<String>()
    private var alphaCodeField = ""
    private var stateCodeField = -1
    private var districtCodeField = -1
    var selectedTaluka = ""
    var latitude: Int? = 0
    var longitude: Int? = 0


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
    fun checkValidation(): Boolean {

        /* Check Username */
        if (firstNameField.get()?.trim().isNullOrEmpty()) {
            /* Notify User */
            isValidFirstName.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_valid_name)
                )
            )
            return false
        }

        if (lastNameField.get()?.trim().isNullOrEmpty()) {
            /* Notify User */
            isValidLastName.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_valid_last_name)
                )
            )
            return false
        }

        if (alternatePhoneFirstNumberField.get()?.trim().isNullOrEmpty()) {
            isValidAlternatePhoneNumberFirst.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_valid)
                )
            )
            return false
        }

        if (alternatePhoneSecondNumberField.get()?.trim().isNullOrEmpty()) {
            isValidAlternatePhoneNumberSecond.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_valid)
                )
            )
            return false
        }

        /* Check Address line 1 */
        if (addressFirstField.get()?.trim()
                .isNullOrEmpty() || (latitude == 0 && longitude == 0)
        ) {
            /* Notify User */
            isValidFirstAddress.set(resourceProvider.getString(R.string.please_enter_valid))
            return false
        }

        if (addressSecondField.get()?.trim().isNullOrEmpty()) {
            isValidSecondAddress.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_valid)
                )
            )
            return false
        }

        if (pinCodeField.get()?.trim().isNullOrEmpty()) {
            isValidPinCode.set(
                BaseModel(
                    message = resourceProvider.getString(R.string.please_enter_valid)
                )
            )
            return false
        }

        return true
    }

    /** To Prevent From BackStack Issue */
    fun clearEditTextValidationErrors() {

        isValidFirstName.set(BaseModel(true))
        isValidLastName.set(BaseModel(true))
        isValidSecondAddress.set(BaseModel(true))
        isValidPinCode.set(BaseModel(true))
        isValidFirstAddress.set("")
        isValidAlternatePhoneNumberFirst.set(BaseModel(true))
        isValidAlternatePhoneNumberSecond.set(BaseModel(true))
    }

    /** On Update Button Click */
    fun updateProfileData() {

        if (checkValidation()) {

            visibleLoader.set(true)
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

            val addressJsonObject = JsonObject()
            val addressLine1JsonObject = JsonObject()
            addressLine1JsonObject.addProperty(
                "en",
                addressFirstField.get()?.trim() ?: ""
            )
            addressLine1JsonObject.addProperty("hi", addressFirstField.get()?.trim() ?: "")

            addressJsonObject.add("line1", addressLine1JsonObject)

            val addressLine2JsonObject = JsonObject()
            addressLine2JsonObject.addProperty(
                "en",
                addressSecondField.get()?.trim() ?: ""
            )
            addressLine2JsonObject.addProperty("hi", addressSecondField.get()?.trim() ?: "")

            addressJsonObject.add("line2", addressLine2JsonObject)

            val stateJsonObject = JsonObject()
            stateJsonObject.addProperty("en", stateField.get())
            stateJsonObject.addProperty("hi", stateField.get())

            addressJsonObject.add("state", stateJsonObject)

            val districtJsonObject = JsonObject()
            districtJsonObject.addProperty("en", districtField.get())
            districtJsonObject.addProperty("hi", districtField.get())

            addressJsonObject.add("district", districtJsonObject)

            val blockJsonObject = JsonObject()
            blockJsonObject.addProperty("en", talukaField.get())
            blockJsonObject.addProperty("hi", talukaField.get())

            addressJsonObject.add("block", blockJsonObject)

            addressJsonObject.addProperty("zipcode", pinCodeField.get())
            addressJsonObject.addProperty("stateCode", stateCodeField)
            addressJsonObject.addProperty("stateCodeAlpha", alphaCodeField)
            addressJsonObject.addProperty("districtCode", districtCodeField)
            addressJsonObject.add("geo", JsonArray().apply {
                add(longitude)
                add(latitude)
            })

            jsonObject.add(
                "address",
                addressJsonObject
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
            Log.e(TAG, "responsee ${jsonObject}")

            /** Call API */
            NetworkModule.retrofit.updateUserProfile(jsonObject)
                .enqueue(object : Callback<UpdateCustomerProfile> {
                    override fun onResponse(
                        call: Call<UpdateCustomerProfile>,
                        response: Response<UpdateCustomerProfile>
                    ) {
                        Log.e(TAG, "response ${response.body()}")

                        visibleLoader.set(false)

                        if (response.isSuccessful) {
                            Log.e(TAG, "response ${response.body()}")
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

                    override fun onFailure(call: Call<UpdateCustomerProfile>, t: Throwable) {

                        visibleLoader.set(false)

                        /* Set the message  */
                        errorMessage.postValue(t.cause.toString())
                    }

                })
        }
    }

    /**
     * Set Profile Data
     * */
    fun setUiData(data: CustomerProfileModel.Data) {

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
            firstNameField.set(it.trim().capitalizeFirstLetter())
        }

        data.lastName?.en?.let {
            lastNameField.set(it.trim().capitalizeFirstLetter())
        }

        data.address?.line1?.en?.let {
            addressFirstField.set(it.trim().capitalizeFirstLetter())
        }

        data.address?.line2?.en?.let {
            addressSecondField.set(it.trim())
        }

        data.address?.state?.en?.let {
            stateField.set(it.capitalizeFirstLetter())
        }

        data.address?.district?.en?.let {
            districtField.set(it.capitalizeFirstLetter())
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

            visibleLoader.set(true)

            NetworkModule.retrofit.getTaluka(pinCode)
                .enqueue(object : Callback<TalukaModel> {
                    override fun onResponse(

                        call: Call<TalukaModel>,
                        response: Response<TalukaModel>
                    ) {
                        visibleLoader.set(false)

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

                                    /* Get Area Related Codes */
                                    getStateDistrictCodes(
                                        stateField.get() ?: "",
                                        districtField.get() ?: ""
                                    )

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

                        visibleLoader.set(false)

                        /*Set the Failure message  */
                        errorMessage.postValue(t.cause.toString())
                    }

                })

        }

    }

    /**
     * Get State District Codes
     * */
    private fun getStateDistrictCodes(state: String, district: String) {

        /* Check Data */
        if (state.trim().isEmpty() || state.trim().isEmpty()) {

            /* Reset Area Data */
            resetStateDistrictBlock()

            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            NetworkModule.retrofit.getStateDistricCodes(
                state.trim().lowercase(),
                district.trim().lowercase()
            )
                .enqueue(object : Callback<StateDistricCodesModel> {
                    override fun onResponse(

                        call: Call<StateDistricCodesModel>,
                        response: Response<StateDistricCodesModel>
                    ) {
                        visibleLoader.set(false)

                        if (response.isSuccessful) {
                            if (response.body()?.status == true && response.body() != null) {

                                response.body()?.data?.let { data ->

                                    data.statecode?.let {
                                        stateCodeField = it.toInt()
                                    }

                                    data.alphacode?.let {
                                        alphaCodeField = it.trim()
                                    }

                                    data.district?.let {
                                        districtCodeField = it.toInt()
                                    }
                                }
                                /*Set the message  */
                                errorMessage.postValue(response.body()?.message)
                            } else {
                                /*Set the message  */
                                errorMessage.postValue(response.body()?.message)
                            }
                        } else {
                            /*Set the message  */
                            errorMessage.postValue(response.message())
                        }
                    }


                    override fun onFailure(call: Call<StateDistricCodesModel>, t: Throwable) {

                        visibleLoader.set(false)

                        /*Set the Failure message  */
                        errorMessage.postValue(t.cause.toString())

                        /* Reset Area Data */
                        resetStateDistrictBlock()
                    }

                })


        }
    }


}