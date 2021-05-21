package com.example.apipractice.view.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.apipractice.R
import com.example.apipractice.basemodel.Constants
import com.example.apipractice.basemodel.Constants.KEY
import com.example.apipractice.databinding.FragmentMyProfileBinding
import com.example.apipractice.datamodel.ProfileModel
import com.example.apipractice.network.ProfileListener
import com.example.apipractice.utills.StorePreferences
import com.example.apipractice.view.fragment.editprofile.EditProfileFragment.Companion.EDITPROFILE
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), ProfileListener {

    lateinit var binding: FragmentMyProfileBinding
    lateinit var viewModel: ProfileVM
    lateinit var storePreferences: StorePreferences

    companion object {
        val LOGOUT = "LOGOUT"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_my_profile, container, false
        )
        viewModel = ViewModelProvider(this).get(ProfileVM::class.java)
        binding.viewModel = viewModel
        /** ProfileListener interface */
        viewModel.listener = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** Initialize StorePreferences */
        storePreferences = StorePreferences(requireContext())

        /** Set Profile Data From DataStore*/
        viewModel.app.getProfileData()?.let { viewModel.setUIData(it) }

        /*** Profile Updated Snackbar */
        when (arguments?.getString(KEY)) {
            EDITPROFILE ->
                Snackbar.make(
                    requireContext(),
                    binding.layout,
                    viewModel.resourceProvider.getString(R.string.profile_update),
                    Snackbar.LENGTH_SHORT
                ).show()
        }
        /** Call API */
        viewModel.getProfileData()

        onClick()
    }

    /** Set Click Listener */
    fun onClick() {

        /** Logout Button Click */
        binding.logout.setOnClickListener {

            /** clear dataStore token and userType */
            GlobalScope.launch {
                storePreferences.storeValue(StorePreferences.Token, "")
                storePreferences.storeValue(StorePreferences.User, "")
                storePreferences.storeValue(StorePreferences.DEMAND_PROFILE_DATA, "")
            }
            val bundle = bundleOf().apply {
                putString(KEY, LOGOUT)
            }
            /** Navigate to Login with bundle */
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment, bundle)
        }

        /** Edit Details Button Click */
        binding.editBasicDetailTextView.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
    }

    /** Api Response success */
    override fun onSuccess(loginResponse: LiveData<ProfileModel>) {
        loginResponse.observe(this, Observer {
            if (it.status == true) {
                GlobalScope.launch {

                    /** Set UI data */
                    it.data?.let { it1 ->
                        viewModel.setUIData(it1)

                        /** Store Profile Data In DataStore */
                        storePreferences.storeValue(StorePreferences.DEMAND_PROFILE_DATA, it1)
                        viewModel.app.setProfileData(it1)
                    }
                }
            } else {
                /** Toast Message*/
                Toast.makeText(
                    requireContext(),
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


}