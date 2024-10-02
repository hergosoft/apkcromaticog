package com.app.g_optics.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

// BroadcastReceiver para detectar cambios en el estado de la red
class NetworkChangeReceiver(private val onNetworkChange: (Boolean) -> Unit) : BroadcastReceiver() {
    // Método que se ejecuta al recibir un evento de cambio de red
    override fun onReceive(context: Context?, intent: Intent?) {
        // Verificar la conexión a Internet
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        // Comprueba si hay conexión a través de Wi-Fi o red celular
        val isConnected = networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))

        // Llama a la función de callback para notificar el estado de la red
        onNetworkChange(isConnected)
    }

    // Función para comprobar si hay acceso a Internet mediante una solicitud HTTP a Google
    private fun isInternetAvailable(): Boolean {
        return try {
            // Realiza una conexión a un servidor (Google) para comprobar el acceso a Internet
            val url = URL("http://www.google.com")
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.setRequestProperty("User-Agent", "test")
            urlConnection.setRequestProperty("Connection", "close")
            urlConnection.setConnectTimeout(1500) // Tiempo de espera de la conexión
            urlConnection.connect()

            // Verifica si el código de respuesta es 200 (OK)
            val responseCode = urlConnection.responseCode
            responseCode == 200
        } catch (e: IOException) {
            false // Si ocurre una excepción, no hay acceso a Internet
        }
    }
}
