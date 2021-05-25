package com.example.apipractice.view.fragment.selectaddress

import android.location.Address
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apipractice.application.MyApplication
import com.example.apipractice.view.listener.ResourceProvider

class SelectAddressVM : ViewModel() {

    /* View Fields */
    val progressLoading = ObservableField(false)
    val currentAddress = ObservableField("")

    /* Initialize ResourceProvider */
    val resourceProvider: ResourceProvider = ResourceProvider(MyApplication.getApplication())

    /* Select Location */
    var selectedAddress: Address? = null

}