package fr.rainbow

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NetworkManager(context: Context) {

    //val context = YourApplication.appContext
    private var networkCallback: ConnectivityManager.NetworkCallback
    private val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _isNetworkAvailable = MutableStateFlow(false)
    val isNetworkAvailable: StateFlow<Boolean> = _isNetworkAvailable.asStateFlow()

    init {
        networkCallback = createNetworkCallback()

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {

            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

            val hasInternetCapability = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)

            if (hasInternetCapability == true) {
                updateNetworkAvailability(true)
            }
        }

        override fun onLost(network: Network) {
            updateNetworkAvailability(false)
        }
    }
    private fun updateNetworkAvailability(newState: Boolean) {
        _isNetworkAvailable.value = newState
    }

}