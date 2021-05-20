package com.example.apipractice.view.fragment.profile

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apipractice.application.MyApplication
import com.example.apipractice.datamodel.DataValue
import com.example.apipractice.datamodel.ProfileData
import com.example.apipractice.datamodel.ProfileModel
import com.example.apipractice.datamodel.ServicesDataList
import com.example.apipractice.network.ProfileListener
import com.example.apipractice.repo.UserRepository
import com.example.apipractice.utills.DateFormatUtils
import com.example.apipractice.utills.StorePreferencesss


class ProfileVM : ViewModel() {

    /* Data Members */
    var profileData: ProfileData? = null
    val app = MyApplication.getApplication()

    //TODO Remove Warnings and Write Proper Comments
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

    /** Health Profile */
    val healthIssuesField = ObservableField("")
    val healthCoverageField = ObservableField(false)
    val healthProviderField = ObservableField("")
    val healthPolicyNumberField = ObservableField("")
    var listener: ProfileListener? = null


    fun getProfileData() {
        visible.set(true)
        val loginResponse = UserRepository().getProfile()
        listener?.onSuccess(loginResponse)
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