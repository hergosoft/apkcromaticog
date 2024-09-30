package com.app.g_optics.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class NetworkChangeReceiver(private val onNetworkChange: (Boolean) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Verificar la conexión a Internet
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        val isConnected = networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))

        // Llamar a la función de callback para notificar el estado de la red
        onNetworkChange(isConnected)
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            // Intenta realizar una conexión a un servidor para verificar el acceso a Internet
            val url = URL("http://www.google.com")
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.setRequestProperty("User-Agent", "test")
            urlConnection.setRequestProperty("Connection", "close")
            urlConnection.setConnectTimeout(1500) // Tiempo de espera de conexión
            urlConnection.connect()
            val responseCode = urlConnection.responseCode
            responseCode == 200 // Verifica si el código de respuesta es 200 (OK)
        } catch (e: IOException) {
            false // Si ocurre una excepción, no hay acceso a Internet
        }
    }
}