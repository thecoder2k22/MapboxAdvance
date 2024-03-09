package com.thecoder.maps.utils

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import java.io.IOException
import java.util.Locale


object LocationTool {

// Location util developed by
    // FAHEEM (thecoder)



    // using GeoCoder to to decode address from coords

    suspend fun getAddressFromLatLong(context: Context,latitude:Double,longitude: Double):String{
        var address=""
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.size > 0) {
                val data = addresses[0]
             address = data.getAddressLine(0) // Get the full address

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return address
    }

    fun isLocationEnabled(context: Context):Boolean{
        val locMan=context.getSystemService(LocationManager::class.java)
       return locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun getLocationEnableIntent(): Intent {
        return Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    }
}