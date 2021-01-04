package com.example.donateapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService

class InternetModel {

    private var internetConnection = false
/*
    @RequiresApi(Build.VERSION_CODES.M)
    var hasInternetType =  isOnline(this)
    //println("Internet  status = "+hasInternetType)

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: InternetModel): String {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {

            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.d("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    var networkvalue = "cellular/4G/5G"
                    internetConnection = true
                    checkInternet(internetConnection)
                    return networkvalue
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    internetConnection = true
                    checkInternet(internetConnection)
                    var networkvalue = "Wifi"
                    return networkvalue
                }
                else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    var networkvalue = "ethernet"
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    internetConnection = true
                    checkInternet(internetConnection)

                    return networkvalue
                }
            }}

        return "none"

    }

    private fun checkInternet(checkInternet : Boolean) {
        if (checkInternet) {
            println("!!! INTERNET ACCESS") // Internet
        }
        else {
            //  Get/Display the cache data
            println("!!! NO ACCESS") // Cache

        }
    } */
}