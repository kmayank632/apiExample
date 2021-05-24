package com.example.apipractice.view.fragment.editprofile

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
import com.example.apipractice.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    companion object {
        val EDITPROFILE = "EDITPROFILE"
    }

    /* ViewBinding Variable */
    lateinit var binding: FragmentEditProfileBinding

    /* ViewModel Variable */
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*Set UI Fields */
        viewModel.app.getProfileData()?.let { viewModel.setUiData(it) }

        /*Call setClick Function*/
        setClick()

        /*Call observe Function*/
        observe()
    }

    /** set click Listner*/
    fun setClick() {

        /* Update Button Click*/
        binding.updateButton.setOnClickListener {
            viewModel.updateProfileData()
        }

        /* Cancel Button Click*/
        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
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
        viewModel.apiResponse.observe(viewLifecycleOwner, {

            /* Check Status*/
            if (it.status == true) {

                /* EditProfile Bundle for Snackbar */
                val bundle = bundleOf().apply {
                    putString(KEY, EDITPROFILE)
                }

                /* Navigate to Profile*/
                findNavController().navigate(
                    R.id.action_editProfileFragment_to_profileFragment,
                    bundle
                )
            }
        })
    }
}