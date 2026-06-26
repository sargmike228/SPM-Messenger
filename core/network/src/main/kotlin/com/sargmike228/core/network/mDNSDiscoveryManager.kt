package com.sargmike228.core.network

import android.content.Context
import android.content.pm.PackageManager
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import androidx.core.content.ContextCompat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class mDNSDiscoveryManager @Inject constructor(private val context: Context) {
    
    private val nsdManager: NsdManager? = context.getSystemService(Context.NSD_SERVICE) as? NsdManager
    
    private val discoveredServices = mutableMapOf<String, NsdServiceInfo>()
    private var discoveryListener: NsdManager.DiscoveryListener? = null
    
    fun startServiceDiscovery(serviceType: String = "_http._tcp.") {
        discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(regType: String) {
                println("Discovery started for: $regType")
            }
            
            override fun onDiscoveryStopped(serviceType: String) {
                println("Discovery stopped")
            }
            
            override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                println("Service found: ${serviceInfo.serviceName}")
                discoveredServices[serviceInfo.serviceName] = serviceInfo
            }
            
            override fun onServiceLost(serviceInfo: NsdServiceInfo) {
                println("Service lost: ${serviceInfo.serviceName}")
                discoveredServices.remove(serviceInfo.serviceName)
            }
            
            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                println("Discovery failed: $errorCode")
            }
            
            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                println("Stop discovery failed: $errorCode")
            }
        }
        
        nsdManager?.discoverServices("_spm._tcp.", NsdManager.PROTOCOL_DNS_SD, discoveryListener)
    }
    
    fun stopServiceDiscovery() {
        discoveryListener?.let {
            nsdManager?.stopServiceDiscovery(it)
        }
    }
    
    fun registerService(serviceName: String, port: Int) {
        val serviceInfo = NsdServiceInfo().apply {
            this.serviceName = serviceName
            this.serviceType = "_spm._tcp."
            this.port = port
        }
        
        nsdManager?.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, object : NsdManager.RegistrationListener {
            override fun onServiceRegistered(nsdServiceInfo: NsdServiceInfo) {
                println("Service registered: ${nsdServiceInfo.serviceName}")
            }
            
            override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                println("Registration failed: $errorCode")
            }
            
            override fun onServiceUnregistered(nsdServiceInfo: NsdServiceInfo) {
                println("Service unregistered")
            }
            
            override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                println("Unregistration failed: $errorCode")
            }
        })
    }
    
    fun getDiscoveredServices(): Map<String, NsdServiceInfo> {
        return discoveredServices.toMap()
    }
}