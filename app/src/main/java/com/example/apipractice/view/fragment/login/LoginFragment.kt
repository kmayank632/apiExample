package com.example.apipractice.view.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.apipractice.R
import com.example.apipractice.basemodel.Constants
import com.example.apipractice.basemodel.Constants.KEY
import com.example.apipractice.databinding.FragmentLoginBinding
import com.example.apipractice.utills.StorePreferences
import com.example.apipractice.view.fragment.profile.ProfileFragment.Companion.LOGOUT
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    /* ViewBinding Variable */
    lateinit var binding: FragmentLoginBinding

    /* ViewModel Variable */
    lateinit var viewModel: LoginVM

    /* StorePreferences Variable */
    lateinit var storePreferences: StorePreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /* Inflate the layout for this fragment */
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        viewModel = ViewModelProvider(this).get(LoginVM::class.java)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Logout Successfully snackbar */
        if (KEY == LOGOUT) {
            Snackbar.make(
                requireContext(),
                binding.layout,
                viewModel.resourceProvider.getString(R.string.logout_successfully),
                Snackbar.LENGTH_SHORT
            ).show()
        }

        /* Initialize DataStore*/
        storePreferences = StorePreferences(requireContext())

        /* Call Click Function*/
        setClick()

        /* Call observe Function*/
        observe()
    }

    /* Set UI Clicks */
    private fun setClick() {
        binding.loginButton.setOnClickListener {
            viewModel.setLogin()
        }
    }

    private fun observe() {

        /* Observe Error*/
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

        /* Observe Response*/
        viewModel.loginResponse.observe(viewLifecycleOwner, {

            /* If User Type is patient*/
            if (it.data?.userType == Constants.USER_TYPE.PATIENT) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }

        })
    }


}