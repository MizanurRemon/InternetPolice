package com.internetpolice.app.connectivity

import android.net.ConnectivityManager
import android.net.Network
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class NetworkCallbackImpl : ConnectivityManager.NetworkCallback() {

    private val _isNetworkAvailable = mutableStateOf(true)
    val isNetworkAvailable: State<Boolean> = _isNetworkAvailable
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        _isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        _isNetworkAvailable.value = false
    }
}