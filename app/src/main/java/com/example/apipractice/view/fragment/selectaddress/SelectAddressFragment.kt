package com.example.apipractice.view.fragment.selectaddress

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.apipractice.BuildConfig
import com.example.apipractice.R
import com.example.apipractice.databinding.FragmentSelectAddressBinding
import com.example.apipractice.utills.isGPSEnabled
import com.example.apipractice.utills.isLocationNetworkProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*

class SelectAddressFragment : Fragment() {

    /* ViewBinding Variable */
    lateinit var binding: FragmentSelectAddressBinding

    /* ViewModel Variable */
    lateinit var viewModel: SelectAddressVM

    private lateinit var builder:AlertDialog.Builder
    /**
     * Keys
     * */
    companion object {
        const val ADDRESS_RESULT = "ADDRESS_RESULT"
    }

    /**
     * Google Map Instance
     * */
    var mGoogleMap: GoogleMap? = null

    /**
     * Location Components
     * */
    private var locationManager: LocationManager? = null
    private val locationListener = LocationListener { location ->
        onLastLocation(location)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /* Inflate the layout for this fragment */
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_select_address, container, false
        )
        viewModel = ViewModelProvider(this).get(SelectAddressVM::class.java)
        binding.viewModel = viewModel
        builder = AlertDialog.Builder(requireContext(),R.style.MyDialogTheme)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Initialize Google Map */
        initGoogleMap()

        /* Set Listener */
        setListener()
    }

    /** Set Listeners to Views */
    private fun setListener() {

        binding.nextButton.setOnClickListener {

            if (viewModel.selectedAddress == null) {

                Snackbar.make(
                    requireContext(),
                    binding.layout,
                    viewModel.resourceProvider.getString(R.string.choose_location),
                    Snackbar.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            /* Pass Result to Previous Screen */
            setFragmentResult(
                ADDRESS_RESULT,
                bundleOf(
                    Pair(ADDRESS_RESULT, viewModel.selectedAddress)
                )
            )

            /* Navigate to Previous Screen */
            findNavController().popBackStack()

        }
    }

    /**
     * Google Map Setup
     * */
    private fun initGoogleMap() {
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mGoogleMap = googleMap

            /* Get Permissions */
            notifyBeforePermission()
        }
    }

    /** Notify Before Ask for Permissions */
    private fun notifyBeforePermission() {

        builder.setTitle(viewModel.resourceProvider.getString(R.string.location_access))
        builder.setMessage(viewModel.resourceProvider.getString(R.string.collect_location_data))
        builder.setPositiveButton(viewModel.resourceProvider.getString(R.string.grant_permission)) { dialog, which ->

            dialog.dismiss()

            /* Check GPS is ON or not */
            checkForGPSPermission()
        }

        builder.setNegativeButton(viewModel.resourceProvider.getString(R.string.cancel)) { dialog, which ->

            dialog.dismiss()

            /* Go to Back Screen */
            findNavController().navigateUp()
        }
        builder.show()

    }

    /**
     * Check if GPS is on or not
     * */
    private fun checkForGPSPermission() {

        if (!requireContext().isGPSEnabled() && !requireContext().isLocationNetworkProvider()) {

            /* Notify User to grant permission */
            builder.setTitle(viewModel.resourceProvider.getString(R.string.location_access))
            builder.setMessage(viewModel.resourceProvider.getString(R.string.please_turn_on_location_services))

            builder.setPositiveButton(viewModel.resourceProvider.getString(R.string.grant_permission)) { dialog, which ->

                if (!requireContext().isGPSEnabled() && !requireContext().isLocationNetworkProvider()) {

                    /* Go to Location Service Settings */
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                } else {

                    /* Dismiss Dialog */
                    dialog.dismiss()

                    /* Check For GPS Permission */
                    checkForGPSPermission()
                }

            }

            builder.setNegativeButton(viewModel.resourceProvider.getString(R.string.cancel)) { dialog, which ->

                /* Dismiss Dialog */
                dialog.dismiss()

                /* Go to Back Screen */
                findNavController().navigateUp()
            }
            builder.show()

            return
        }

        askLocationPermission()
    }

    /** Ask For Permission */
    private fun askLocationPermission() {
        locationPermissionResult.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
    }

    /**
     * Callback from Permission Results
     * */
    private var locationPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { grantResults ->
            if (grantResults[Manifest.permission.ACCESS_FINE_LOCATION] == true) {

                /* Initialize Location Components */
                initializeLocationComponents()
            } else {

                builder.setTitle(viewModel.resourceProvider.getString(R.string.permission_required))
                builder.setMessage(viewModel.resourceProvider.getString(R.string.provide_permission))

                builder.setPositiveButton(viewModel.resourceProvider.getString(R.string.grant_permission)) { dialog, which ->

                    /* Dismiss Dialog */
                    dialog.dismiss()

                    /* Open App Setting Activity */
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context?.startActivity(intent)

                    /* Notify to Exit App */
//                    viewModel.eventListener.closeApplication(true)

                }

                builder.setNegativeButton(viewModel.resourceProvider.getString(R.string.cancel)) { dialog, which ->

                    dialog.dismiss()

                    /* Go to Back Screen */
                    findNavController().navigateUp()
                }
                builder.show()

            }
        }

    /**
     * Initialize Location Components
     * */
    private fun initializeLocationComponents() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notifyBeforePermission()
            return
        }

        mGoogleMap?.isMyLocationEnabled = true
        mGoogleMap?.uiSettings?.isMyLocationButtonEnabled = true
        mGoogleMap?.uiSettings?.isZoomControlsEnabled = true
        mGoogleMap?.setPadding(
            0,
            0,
            0,
            viewModel.resourceProvider.getDimension(R.dimen._120sdp).toInt()
        )
        mGoogleMap?.setOnCameraMoveListener {

            mGoogleMap?.clear()

            mGoogleMap?.cameraPosition?.target?.let {
                onLastLocation(Location(LocationManager.GPS_PROVIDER).apply {
                    latitude = it.latitude
                    longitude = it.longitude
                })
            }
        }

        /* Initialize Location Manager */
        if (locationManager == null) {
            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        if (requireContext().isGPSEnabled()) {
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                0F,
                locationListener
            )

            locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let { location ->
                onLastLocation(location)
            }
        }

        if (requireContext().isLocationNetworkProvider()) {
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000,
                0F,
                locationListener
            )

            locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                ?.let { location ->
                    onLastLocation(location)
                }
        }

        /* Initialize Callback to get location updates */
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                onLastLocation(location)
            }
    }

    /**
     * Handle Last Location Result Data
     * */
    var locationJob: Job? = null
    private fun onLastLocation(location: Location?) {
        locationJob?.cancel()
        locationJob = CoroutineScope(Dispatchers.IO).launch {
            location?.let { locationValue ->

                locationManager?.removeUpdates(locationListener)

                viewModel.progressLoading.set(true)
                viewModel.currentAddress.set("")
                viewModel.selectedAddress = null

                delay(2000)

                var address = "No known address"

                val gcd = Geocoder(requireContext(), Locale.getDefault())
                val addresses: List<Address>
                try {
                    addresses =
                        gcd.getFromLocation(locationValue.latitude, locationValue.longitude, 1)
                    if (addresses.isNotEmpty()) {
                        viewModel.selectedAddress = addresses[0]
                        address = addresses[0].getAddressLine(0)
                        viewModel.currentAddress.set(address)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                withContext(Dispatchers.Main) {
                    mGoogleMap?.clear()

                    val icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(
                            resources,
                            R.drawable.ic_pickup
                        )
                    )
                    mGoogleMap?.addMarker(
                        MarkerOptions()
                            .position(LatLng(locationValue.latitude, locationValue.longitude))
                            .title(
                                viewModel.resourceProvider.getString(
                                    R.string.current_location
                                )
                            )
                            .snippet(address)
                            .icon(icon)
                    )

                    val cameraPosition = CameraPosition.Builder()
                        .target(LatLng(locationValue.latitude, locationValue.longitude))
                        .zoom(17f)
                        .build()

                    mGoogleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                    viewModel.progressLoading.set(false)
                }
            }
        }
    }

}


