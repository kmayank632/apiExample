package com.example.apipractice.view.fragment.profile

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apipractice.application.MyApplication
import com.example.apipractice.datamodel.DataValue
import com.example.apipractice.datamodel.ProfileData
import com.example.apipractice.datamodel.ProfileModel
import com.example.apipractice.datamodel.ServicesDataList
import com.example.apipractice.network.NetworkModule
import com.example.apipractice.utills.DateFormatUtils
import com.example.apipractice.utills.StorePreferences
import com.example.apipractice.view.listener.ResourceProvider
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileVM : ViewModel() {

    /* Data Members */
    var profileData: ProfileData? = null

    /* Initialize ResourceProvider */
    val resourceProvider: ResourceProvider = ResourceProvider(MyApplication.getApplication())

    /* Initialize MyApplication */
    val app = MyApplication.getApplication()

    /* Initialize the StorePreferences */
    var storePreferences: StorePreferences = StorePreferences(app)

    /* MutableLiveData Variable to Store Error  */
    var errorMessage = MutableLiveData<String?>()


    /* Ui Fields */
    val visible = ObservableBoolean(false)
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

    val apiResponse = MutableLiveData<ProfileModel>()


    /**
     * Get Profile Data
     * */
    fun getProfileData() {

        /* Set Progress Bar Visibility Visible*/
        visible.set(true)

        NetworkModule.retrofit.getProfile()
            .enqueue(object : Callback<ProfileModel> {
                override fun onResponse(

                    call: Call<ProfileModel>,
                    response: Response<ProfileModel>
                ) {
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


                override fun onFailure(call: Call<ProfileModel>, t: Throwable) {

                    /*Set the Failure message  */
                    errorMessage.postValue(t.cause.toString())
                }

            })

        /* Set Progress Bar Visibility Gone*/
        visible.set(false)


    }

    /**Set Logout*/

    fun setLogout() {
        /* Clear DataStore Data */
        viewModelScope.launch {
            storePreferences.storeValue(StorePreferences.Token, "")
            storePreferences.storeValue(StorePreferences.User, "")
            storePreferences.storeValue(StorePreferences.DEMAND_PROFILE_DATA, "")
        }
    }

    /**
     * Set Ui Data
     * */
    fun setUIData(data: ProfileData) {

        profileData = data

        medoPlusIdField.set(data.medoplusId ?: "")

        data.firstName?.en?.let {
            firstNameField.set(it.trim())
        }

        data.lastName?.en?.let {
            lastNameField.set(it.trim())
        }

        data.address?.line1?.en?.let {
            addressFirstField.set(it.trim())
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

                        val id = it._id
                        val title = DataValue(en = it.name?.en ?: "", hi = it.name?.hi ?: "")
                        if (!id?.trim().isNullOrEmpty()) {
                            val selectedIssue = ServicesDataList(_id = id, title = title)
                            healthIssuesField.set(
                                selectedIssue.title?.en?.trim() ?: ""
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
                    healthProviderField.set(it.trim())
                }

                healthData.policyNumber?.let {
                    healthPolicyNumberField.set(it.trim())
                }
            }
        }

        data.pictures?.let {
            if (it.isNotEmpty()) {
                profilePictureField.set(it[0].preview.toString())
            }
        }
    }
}