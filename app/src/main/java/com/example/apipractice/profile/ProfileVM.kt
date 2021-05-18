package com.example.apipractice.profile

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apipractice.application.DateFormatUtils
import com.example.apipractice.datamodel.DataValue
import com.example.apipractice.datamodel.ProfileData
import com.example.apipractice.datamodel.ProfileModel
import com.example.apipractice.datamodel.ServicesDataList
import com.example.apipractice.network.MyApi
import com.example.apipractice.network.ProfileListner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileVM : ViewModel() {

    /* Data Members */
    var profileData: ProfileData? = null


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
    var authListner: ProfileListner? = null
    val loginResponse = MutableLiveData<ProfileModel>()

    fun getProfileData() {
      visible.set(true)
        MyApi().getProfile()
            .enqueue(object : Callback<ProfileModel> {
                override fun onResponse(
                    call: Call<ProfileModel>,
                    response: Response<ProfileModel>
                ) {
                    if (response.isSuccessful) {
                        loginResponse.value = response.body()
                        response.body()?.data?.let { setUIData(it) }
                        Log.e(TAG, "profile ${response.body()}")

                    }
                }

                override fun onFailure(call: Call<ProfileModel>, t: Throwable) {

                }

            })
        visible.set(false)
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
                profilePictureField.set(it[0].preview)
            }
        }
    }
}