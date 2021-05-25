package com.example.apipractice.utills

import android.content.Context
import android.location.LocationManager

/**
 * Location Check Extension
 * */

fun Context.isGPSEnabled(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    locationManager?.let {
        return locationManager.isProviderEnabled(LocationManager. GPS_PROVIDER)
    }
    return false
}

fun Context.isLocationNetworkProvider(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    locationManager?.let {
        return locationManager.isProviderEnabled(LocationManager. NETWORK_PROVIDER)
    }
    return false
}