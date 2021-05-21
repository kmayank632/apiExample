package com.example.apipractice.view.fragment.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.apipractice.R
import com.example.apipractice.basemodel.Constants.KEY
import com.example.apipractice.databinding.FragmentEditProfileBinding
import com.example.apipractice.datamodel.ProfileModel
import com.example.apipractice.network.ProfileListener

class EditProfileFragment : Fragment(), ProfileListener {
    companion object {
        val EDITPROFILE = "EDITPROFILE"

    }

    lateinit var binding: FragmentEditProfileBinding
    lateinit var viewModel: EditProfileVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_profile, container, false
        )
        viewModel = ViewModelProvider(this).get(EditProfileVM::class.java)
        binding.viewModel = viewModel
        viewModel.profileAuthListener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**Set UI Fields */
        viewModel.app.getProfileData()?.let { viewModel.setUiData(it) }

        /**Call setClick Function*/
        setClick()
    }

    /** set click Listner*/
    fun setClick() {
        binding.updateButton.setOnClickListener {
            viewModel.updateProfileData()
        }
        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    /** Api Success*/
    override fun onSuccess(loginResponse: LiveData<ProfileModel>) {
        loginResponse.observe(this, Observer {

            val bundle = bundleOf().apply {
                putString(KEY, EDITPROFILE)
            }
            /**Navigate to Profile*/
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment, bundle)
        })
    }


}