package com.example.apipractice.view.fragment.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.apipractice.BuildConfig
import com.example.apipractice.R
import com.example.apipractice.basemodel.Constants.KEY
import com.example.apipractice.databinding.FragmentMyProfileBinding
import com.example.apipractice.network.UploadRequestBody
import com.example.apipractice.utills.LogType
import com.example.apipractice.utills.MediaUtils
import com.example.apipractice.utills.logger
import com.example.apipractice.view.fragment.editprofile.EditProfileFragment.Companion.EDITPROFILE
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ProfileFragment : Fragment() ,
    UploadRequestBody.UploadCallback{

    /** Media File Data Members */
    private var mPictureFilePath: String = ""
    private var mPictureFileURI: Uri? = null

    private lateinit var builder: AlertDialog.Builder



    /* ViewBinding Variable */
    lateinit var binding: FragmentMyProfileBinding

    /* ViewModel Variable */
    lateinit var viewModel: ProfileVM

    companion object {
        const val LOGOUT = "LOGOUT"
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
        builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
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

        binding.editImage.setOnClickListener {
            chooseImageSource()
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

    /**
     * Show Dialog to Select Image Source
     * */
    private fun chooseImageSource() {

        /* set message for alert dialog */
        builder.setMessage(R.string.choose_image_source)

        /* Performing positive action */
        builder.setPositiveButton(viewModel.resourceProvider.getString(R.string.camera)) { dialog, which ->
            /* Ask For Permission */
            askCameraPermission()
        }

        /* Performing negative action */
        builder.setNegativeButton(viewModel.resourceProvider.getString(R.string.gallery)) { dialog, which ->
            /* Launch Image Picker */
            launchImagePicker()
        }

        builder.show()

    }

    /**
     * Launch Image Picker
     * */
    private fun launchImagePicker() {
        val pickPhoto = Intent(Intent.ACTION_PICK)
        pickPhoto.type = "image/*"
        if (pickPhoto.resolveActivity(requireContext().packageManager) != null) {
            cameraPickerActivityResult.launch(pickPhoto)
        } else {

            /* Notify User */
            Snackbar.make(
                requireContext(),
                binding.layout,
                viewModel.resourceProvider.getString(R.string.no_relevant_app),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Callback from Activity Results
     * */
    private var cameraPickerActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                /* Get Intent Data */
                val data = result.data

                val exceptionHandler = CoroutineExceptionHandler { _, t ->
                    t.printStackTrace()

                    /* Show Error Message */
                    builder.setMessage(
                        t.localizedMessage
                            ?: viewModel.resourceProvider.getString(R.string.error_message)
                    )

                    builder.show()

                }

                CoroutineScope(Dispatchers.Main + exceptionHandler).launch {

                    var queryImageUrl = ""

                    if (data?.data != null) {

                        /* For Gallery Selection */
                        mPictureFileURI = data.data

                        mPictureFileURI?.let { uri ->
                            mPictureFileURI?.path?.let { path ->
                                queryImageUrl = path
                                queryImageUrl = MediaUtils().compressImageFile(
                                    requireContext(), queryImageUrl, false, uri
                                )
                            }
                        }
                    } else {

                        /* For Camera Capture */
                        queryImageUrl = mPictureFilePath

                        mPictureFileURI?.let { uri ->
                            MediaUtils().compressImageFile(
                                requireContext(),
                                queryImageUrl, uri = uri
                            )
                        }
                    }
                    val file = File(queryImageUrl)

                    /* Update URI Variable */
                    mPictureFileURI = Uri.fromFile(file)

                    viewModel.uploadImage(file,this@ProfileFragment)

                }
            }
        }

    /** Launch Camera App and take Picture */
    private fun launchCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireContext().packageManager) != null) {
            val pictureFile: File?
            try {
                pictureFile = MediaUtils().getPictureFile(requireContext())
                mPictureFilePath = pictureFile.absolutePath
            } catch (e: Exception) {

                logger(javaClass.simpleName, "launchCamera: ${e.message}", LogType.ERROR)

                /* Notify User */
                builder.setMessage(viewModel.resourceProvider.getString(R.string.error_message))
                builder.show()
                return
            }

            if (pictureFile.exists()) {
                pictureFile.deleteOnExit()
            }

            mPictureFileURI = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                /*
                * Add Provider XML file and add in Manifest
                * */
                FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().applicationContext.packageName + ".provider",
                    pictureFile
                )
            } else {
                /* Below 23 Version Api get Uri directly */
                Uri.fromFile(pictureFile)
            }

            mPictureFileURI?.let {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, it)
                cameraPickerActivityResult.launch(cameraIntent)
                return
            }

            /* Notify User */
            builder.setMessage(viewModel.resourceProvider.getString(R.string.error_message))
            builder.show()

        } else {

            /* Notify User */
            Snackbar.make(
                requireContext(),
                binding.layout,
                viewModel.resourceProvider.getString(R.string.no_relevant_app),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Callback from Permission Results
     * */
    private var cameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { grantResults ->
            if (grantResults[Manifest.permission.CAMERA] == true) {

                /* Launch Camera */
                launchCamera()
            } else {
                /* Notify User */
                Snackbar.make(
                    requireContext(),
                    binding.layout,
                    viewModel.resourceProvider.getString(R.string.camera_permission_denied),
                    Snackbar.LENGTH_SHORT
                ).show()

                /* Notify User to grant permission */
                builder.setTitle(viewModel.resourceProvider.getString(R.string.permission_required))

                builder.setMessage(viewModel.resourceProvider.getString(R.string.provide_permission))

                //performing positive action
                builder.setPositiveButton(viewModel.resourceProvider.getString(R.string.grant_permission)) { dialog, which ->

                    /* Open App Setting Activity */
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context?.startActivity(intent)
                }

                //performing negative action
                builder.setNegativeButton(viewModel.resourceProvider.getString(R.string.grant_permission)) { dialog, which ->
                    dialog.dismiss()
                }

                builder.show()
            }
        }


    /**
     * Ask for Camera Permissions
     * */
    private fun askCameraPermission() {
        cameraPermissionResult.launch(arrayOf(Manifest.permission.CAMERA))
    }

    override fun onProgressUpdate(key: String?, percentage: Int) {

    }


}