package com.apps.omdbmovie.presentation.ui.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun checkInternetConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}

fun observeConnectivityAsFlow(context: Context): StateFlow<Boolean> {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkRequest = NetworkRequest.Builder().build()
    val connectivityStatus = MutableStateFlow(checkInternetConnection(context))

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            connectivityStatus.value = true
        }

        override fun onLost(network: Network) {
            connectivityStatus.value = false
        }
    }

    connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

    return connectivityStatus
}