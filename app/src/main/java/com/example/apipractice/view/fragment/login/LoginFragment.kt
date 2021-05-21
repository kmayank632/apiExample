package com.example.apipractice.view.fragment.login

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.apipractice.R
import com.example.apipractice.basemodel.Constants
import com.example.apipractice.databinding.FragmentLoginBinding
import com.example.apipractice.datamodel.LoginModel
import com.example.apipractice.network.AuthListener
import com.example.apipractice.utills.StorePreferences
import com.example.apipractice.view.fragment.profile.ProfileFragment.Companion.LOGOUT
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment(), AuthListener {

    lateinit var binding: FragmentLoginBinding
    lateinit var viewModel: LoginVM
    lateinit var storePreferences: StorePreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        viewModel = ViewModelProvider(this).get(LoginVM::class.java)
        binding.viewModel = viewModel
        viewModel.authListener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Initialize DataStore*/
        storePreferences = StorePreferences(requireContext())

        /** Logout Successfully snackbar */
        when (arguments?.getString(Constants.KEY)) {
            LOGOUT ->
                Snackbar.make(
                    requireContext(),
                    binding.layout,
                    viewModel.resourceProvider.getString(R.string.logout_successfully),
                    Snackbar.LENGTH_SHORT
                ).show()

        }
        setClick()
    }

    /** Set UI Clicks*/
    fun setClick() {
        binding.loginButton.setOnClickListener {
            viewModel.setLogin()
        }
    }

    override fun onSuccess(loginResponse: LiveData<LoginModel>) {
        loginResponse.observe(this, Observer {
            if (it.status == true) {
                /** Store Token in DataStore*/
                GlobalScope.launch {
                    it.data?.token?.let { it1 ->
                        storePreferences.storeValue(
                            StorePreferences.Token,
                            it1
                        )
                    }
                    viewModel.app.setToken(it.data?.token)

                    /** Store UseType in DataStore*/
                    it.data?.userType?.let { it1 ->
                        storePreferences.storeValue(
                            StorePreferences.User,
                            it1
                        )
                    }
                }
                Log.e(TAG, "response $it")

                /** Toast Message*/
                Toast.makeText(
                    requireContext(),
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()

                /** If User Type is patient*/
                if (it.data?.userType == Constants.USER_TYPE.PATIENT) {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
            else{
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