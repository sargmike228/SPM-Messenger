package com.sargmike228.core.network

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothP2PManager @Inject constructor(private val context: Context) {
    
    private val bluetoothManager: BluetoothManager? = 
        context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter
    
    private val activeConnections = mutableMapOf<String, BluetoothSocket>()
    
    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled ?: false
    }
    
    fun enableBluetooth() {
        if (!isBluetoothEnabled()) {
            bluetoothAdapter?.enable()
        }
    }
    
    fun discoverDevices() {
        if (hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            bluetoothAdapter?.startDiscovery()
        }
    }
    
    fun connectToDevice(deviceAddress: String): Boolean {
        return try {
            val device = bluetoothAdapter?.getRemoteDevice(deviceAddress)
            if (hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                val socket = device?.createRfcommSocketToServiceRecord(
                    java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                )
                socket?.connect()
                if (socket != null) {
                    activeConnections[deviceAddress] = socket
                    true
                } else false
            } else false
        } catch (e: Exception) {
            false
        }
    }
    
    fun sendData(deviceAddress: String, data: ByteArray) {
        try {
            val socket = activeConnections[deviceAddress]
            socket?.outputStream?.write(data)
            socket?.outputStream?.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun receiveData(deviceAddress: String): ByteArray? {
        return try {
            val socket = activeConnections[deviceAddress]
            val buffer = ByteArray(1024)
            val bytes = socket?.inputStream?.read(buffer)
            if (bytes != null && bytes > 0) {
                buffer.copyOf(bytes)
            } else null
        } catch (e: Exception) {
            null
        }
    }
    
    fun disconnect(deviceAddress: String) {
        try {
            activeConnections[deviceAddress]?.close()
            activeConnections.remove(deviceAddress)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}