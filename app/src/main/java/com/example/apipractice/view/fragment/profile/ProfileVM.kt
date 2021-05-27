package com.example.apipractice.view.fragment.profile

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apipractice.application.MyApplication
import com.example.apipractice.basemodel.Constants
import com.example.apipractice.datamodel.CustomerProfileModel
import com.example.apipractice.datamodel.UpdateCustomerProfile
import com.example.apipractice.datamodel.UploadFileModel
import com.example.apipractice.network.NetworkModule
import com.example.apipractice.network.UploadRequestBody
import com.example.apipractice.utills.DateFormatUtils
import com.example.apipractice.utills.StorePreferences
import com.example.apipractice.utills.TAG
import com.example.apipractice.view.listener.ResourceProvider
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class ProfileVM : ViewModel() {

    /* Data Members */
    var profileData: CustomerProfileModel.Data? = null

    /*for Capitalize First Letter*/
    fun String.capitalizeFirstLetter() = split(" ").map {
        it.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }.joinToString(" ")

    /* Initialize ResourceProvider */
    val resourceProvider: ResourceProvider = ResourceProvider(MyApplication.getApplication())

    /* Initialize MyApplication */
    val app = MyApplication.getApplication()

    /* Initialize the StorePreferences */
    var storePreferences: StorePreferences = StorePreferences(app)

    /* MutableLiveData Variable to Store Error  */
    var errorMessage = MutableLiveData<String?>()


    /* Ui Fields */
    val visibleLoader = ObservableBoolean(false)
    val profilePictureField = ObservableField("")
    val medoPlusIdField = ObservableField("")
    val firstNameField = ObservableField("")
    val lastNameField = ObservableField("")
    val dobField = ObservableField("")
    val addressFirstField = ObservableField("")
    val phoneNumberField = ObservableField("")
    val bloodGroup = ObservableField("")

    /* Health Profile */
    val healthIssuesField = ObservableField("")
    val healthCoverageField = ObservableField(false)
    val healthProviderField = ObservableField("")
    val healthPolicyNumberField = ObservableField("")

    val apiResponse = MutableLiveData<CustomerProfileModel>()


    /**
     * Get Profile Data
     * */
    fun getProfileData() {

        /* Set Progress Bar Visibility Visible*/
        visibleLoader.set(true)

        NetworkModule.retrofit.getProfile()
            .enqueue(object : Callback<CustomerProfileModel> {
                override fun onResponse(
                    call: Call<CustomerProfileModel>,
                    response: Response<CustomerProfileModel>
                ) {
                    /* Set Progress Bar Visibility Gone*/
                    visibleLoader.set(false)

                    if (response.isSuccessful) {
                        if (response.body()?.status == true && response.body() != null) {

                            apiResponse.postValue(response.body())

                            viewModelScope.launch {
                                response.body()?.data?.let { it1 ->

                                    /* call setUIData  */
                                    setUIData(it1)

                                    /* Store Profile Data In DataStore */
                                    storePreferences.storeValue(
                                        StorePreferences.DEMAND_PROFILE_DATA,
                                        it1
                                    )
                                    app.setProfileData(it1)
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


                override fun onFailure(call: Call<CustomerProfileModel>, t: Throwable) {

                    /* Set Progress Bar Visibility Gone*/
                    visibleLoader.set(false)

                    /*Set the Failure message  */
                    errorMessage.postValue(t.cause.toString())
                }

            })




    }

    /**Set Logout*/

    fun setLogout() {
        /* Clear DataStore Data */
        viewModelScope.launch {
            storePreferences.storeValue(StorePreferences.User, "")
            storePreferences.storeValue(StorePreferences.Token, "")
            storePreferences.storeValue(StorePreferences.DEMAND_PROFILE_DATA, "")
        }
    }


    /**
     * Set Ui Data
     * */
    fun setUIData(data: CustomerProfileModel.Data) {

        profileData = data

        medoPlusIdField.set(data.medoplusId ?: "")

        data.firstName?.en?.let {
            firstNameField.set(it.trim().capitalizeFirstLetter())
        }

        data.lastName?.en?.let {
            lastNameField.set(it.trim().capitalizeFirstLetter())
        }

        data.address?.line1?.en?.let {
            addressFirstField.set(it.trim().capitalizeFirstLetter())
        }

        data.number?.let {
            phoneNumberField.set(it)
        }

        data.bloodGroup?.let {
            bloodGroup.set(it)
        }

        data.dob?.let {
            dobField.set(
                DateFormatUtils().getDateByFormatCustomUTC(
                    it,
                    true,
                    DateFormatUtils.DATE_TIME_FORMAT.MONOGO_DB_UTC, false,
                    DateFormatUtils.DATE_TIME_FORMAT.dd_MM_yyyy
                )
            )
        }

        data.existingAilments?.let { ailment ->

            ailment.ailment?.let { list ->

                if (list.isNotEmpty()) {
                    list[0].let {

                        val id = it?._id
                        val title = CustomerProfileModel.Data.ExistingAilments.Ailment.Name(
                            en = it?.name?.en ?: ""
                        )
                        if (!id?.trim().isNullOrEmpty()) {
                            val selectedIssue = CustomerProfileModel.Data.ExistingAilments.Ailment(
                                _id = id,
                                name = title,
                                null
                            )
                            healthIssuesField.set(
                                selectedIssue.name?.en?.trim() ?: ""
                            )
                        }
                    }
                }
            }
        }

        data.healthInsurance?.let { healthData ->

            healthData.isActive?.let {
                healthCoverageField.set(it)
            }

            if (healthCoverageField.get() == true) {

                healthData.companyName?.let {
                    healthProviderField.set(it.trim().capitalizeFirstLetter())
                }

                healthData.policyNumber?.let {
                    healthPolicyNumberField.set(it.trim())
                }
            }
        }

        data.pictures?.let {
            if (it.isNotEmpty()) {
                profilePictureField.set(it[0]?.preview.toString())
            }

        }
    }


    /**
     * Upload Image to Server
     * */
    fun uploadImage(file: File, callback: UploadRequestBody.UploadCallback) =
        viewModelScope.launch {

            /* Set Progress Bar Visibility Gone*/
            visibleLoader.set(true)

            /* Prepare Request BODY */
            val body = UploadRequestBody(file, "image" /* Type of  */, callback)
            val multipart = MultipartBody.Part.createFormData(
                "files",
                file.name,
                body
            )
            val thumbnail =
                true.toString().toRequestBody(("multipart/form-data").toMediaTypeOrNull())

            NetworkModule.retrofit.uploadCustomerImage(thumbnail, multipart)
                .enqueue(object : Callback<UploadFileModel> {
                    override fun onResponse(
                        call: Call<UploadFileModel>,
                        response: Response<UploadFileModel>
                    ) {
                        /* Set Progress Bar Visibility Gone*/
                        visibleLoader.set(false)

                        if (response.isSuccessful) {
                            if (response.body()?.status == true && response.body() != null) {

                                /* Store UseType in DataStore*/
                                viewModelScope.launch {
                                    response.body()?.data?.forEach { item ->
                                        if (item.type == Constants.IMAGE_TYPE.ORIGINAL) {
                                            item.path?.let {
                                                saveDataOnServer(it)
                                                return@launch
                                            }
                                        }
                                    }
                                }
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

                    override fun onFailure(call: Call<UploadFileModel>, t: Throwable) {

                        /* Set Progress Bar Visibility Gone*/
                        visibleLoader.set(false)

                        /*Set the Failure message  */
                        errorMessage.postValue(t.cause.toString())
                    }

                })

        }


    /**
     * Save and Complete SignUp Form
     * */
    fun saveDataOnServer(imageUrl: String) {

        /* Set Progress Bar Visibility Gone*/
        visibleLoader.set(true)

        viewModelScope.launch(Dispatchers.IO) {

            /* Prepare Request Body */
            val jsonObject = JsonObject()
            val pictures = JsonArray()
            val jsonTypeObject = JsonObject()
            jsonTypeObject.addProperty("type", Constants.IMAGE_TYPE.THUMBNAIL)
            jsonTypeObject.addProperty("url", imageUrl)
            pictures.add(jsonTypeObject)
            jsonObject.add("pictures", pictures)

            /** Call API */
            NetworkModule.retrofit.updateUserProfile(jsonObject)
                .enqueue(object : Callback<UpdateCustomerProfile> {
                    override fun onResponse(
                        call: Call<UpdateCustomerProfile>,
                        response: Response<UpdateCustomerProfile>
                    ) {
                        Log.e(TAG, "response ${response.body()}")

                        /* Set Progress Bar Visibility Gone*/
                        visibleLoader.set(false)

                        if (response.isSuccessful) {
                            Log.e(TAG, "response ${response.body()}")
                            if (response.body() != null && response.body()?.status == true) {

                                viewModelScope.launch {
                                    response.body()?.data?.let { it1 ->

                                        /* Store Profile Data In DataStore */
                                        storePreferences.storeValue(
                                            StorePreferences.DEMAND_PROFILE_DATA,
                                            it1
                                        )
                                        profilePictureField.set(it1.pictures?.get(0)?.preview)

                                    }
                                }

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

                        /* Set Progress Bar Visibility Gone*/
                        visibleLoader.set(false)

                        /* Set the message  */
                        errorMessage.postValue(t.cause.toString())
                    }

                })
        }
    }
}