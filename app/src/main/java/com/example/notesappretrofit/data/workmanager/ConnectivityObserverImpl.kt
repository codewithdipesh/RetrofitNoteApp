package com.example.notesappretrofit.data.workmanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.example.notesappretrofit.presentation.home.elements.ConnectivityObserver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConnectivityObserverImpl @Inject constructor(
    @ApplicationContext private val context: Context
):ConnectivityObserver{

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    override fun observer(): Flow<ConnectivityObserver.Status> {
       return callbackFlow {
           val callback = object : ConnectivityManager.NetworkCallback(){
               override fun onAvailable(network: Network) {
                   super.onAvailable(network)
                   launch { send(ConnectivityObserver.Status.Available) }
               }
               override fun onLost(network: Network) {
                   super.onLost(network)
                   launch { send(ConnectivityObserver.Status.Lost) }
               }

               override fun onUnavailable() {
                   super.onUnavailable()
                   launch { send(ConnectivityObserver.Status.Unavailable) }
               }
           }

           connectivityManager.registerDefaultNetworkCallback(callback)
           awaitClose {//when viewmodelscope ended it will lcosed
               //clean up what it need to be
               connectivityManager.unregisterNetworkCallback(callback)

           }
       }.distinctUntilChanged()
    }
}