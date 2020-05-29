package com.remcode.coloursforyou.utils

import android.app.Application
import android.content.Context
import android.net.*
import androidx.lifecycle.LiveData

/**
 * This object is used to handle network connectivity issues.
 * It implements LiveData to get the current status of a network
 */
object NetworkStatusLiveData : LiveData<NetworkStatus>() {
    private lateinit var application: Application

    private lateinit var networkRequest: NetworkRequest

    fun init(application: Application) {
        this.application = application
        networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
    }

    override fun onActive() {
        super.onActive()
        getStatus()
    }

    private fun getStatus() {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        cm.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                if (network != null) {
                    super.onAvailable(network)
                    postValue(NetworkStatus.AVAILABLE)
                } // else log something
            }

            override fun onUnavailable() {
                super.onUnavailable()
                postValue(NetworkStatus.UNAVAILABLE)
            }

            override fun onLost(network: Network?) {
                if(network != null) {
                    super.onLost(network)
                    postValue(NetworkStatus.LOST)
                }
            }
        })
    }
}

enum class NetworkStatus {
    AVAILABLE, UNAVAILABLE, LOST
}