package com.example.apipractice.view.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.apipractice.R
import com.example.apipractice.basemodel.Constants.KEY
import com.example.apipractice.databinding.FragmentMyProfileBinding
import com.example.apipractice.view.fragment.editprofile.EditProfileFragment.Companion.EDITPROFILE
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : Fragment() {

    /* ViewBinding Variable */
    lateinit var binding: FragmentMyProfileBinding

    /* ViewModel Variable */
    lateinit var viewModel: ProfileVM

    companion object {
        val LOGOUT = "LOGOUT"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /* Inflate the layout for this fragment */
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_my_profile, container, false
        )
        viewModel = ViewModelProvider(this).get(ProfileVM::class.java)
        binding.viewModel = viewModel

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /* Profile Updated Snackbar */
        when (arguments?.getString(KEY)) {
            EDITPROFILE ->
                Snackbar.make(
                    requireContext(),
                    binding.layout,
                    viewModel.resourceProvider.getString(R.string.profile_update),
                    Snackbar.LENGTH_SHORT
                ).show()
        }

        /* Set Profile Data From DataStore*/
        viewModel.app.getProfileData()?.let { viewModel.setUIData(it) }

        /* Call API */
        viewModel.getProfileData()

        /* Call Click Function*/
        onClick()

        /* Call observe Function*/
        observe()
    }

    /* Set Click Listener */
    private fun onClick() {

        /* Logout Button Click */
        binding.logout.setOnClickListener {

            /* Call Logout Function*/
            viewModel.setLogout()

            val bundle = bundleOf().apply {
                putString(KEY, LOGOUT)
            }
            /* Navigate to Login with bundle */
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment, bundle)
        }

        /* Edit Details Button Click */
        binding.editBasicDetailTextView.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
    }

    /*Observe Data*/
    private fun observe() {
        viewModel.errorMessage.observe(viewLifecycleOwner, {
            if (it != null) {
                /* Toast Message*/
                Toast.makeText(
                    requireContext(),
                    viewModel.errorMessage.value,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


}