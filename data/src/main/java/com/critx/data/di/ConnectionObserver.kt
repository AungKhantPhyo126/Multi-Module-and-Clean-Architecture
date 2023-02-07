package com.critx.data.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Helper class for observing WIFI and CELLULAR network changes
 */
interface ConnectionObserver {

    var onConnected: () -> Unit
    var onDisconnected: () -> Unit
    var onUnAvailable: () -> Unit
    var onLosing: () ->Unit

    fun register()

    fun unregister()
}

class ConnectionObserverImpl @Inject constructor(
    @ApplicationContext context: Context
): ConnectionObserver {

    private val connectivityManager = context.getSystemService<ConnectivityManager>()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            onConnected()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            onDisconnected()
        }

        override fun onUnavailable() {
            super.onUnavailable()
            onUnAvailable()
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            onLosing()
        }

    }
    override var onConnected: () -> Unit = {}
    override var onDisconnected: () -> Unit = {}
    override var onLosing: () -> Unit = {}
    override var onUnAvailable: () -> Unit = {}


    override fun register() {
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun unregister() {
        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }
}