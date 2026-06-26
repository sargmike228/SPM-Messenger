package com.sargmike228.feature.messaging.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.sargmike228.core.network.DirectSocketP2PManager
import com.sargmike228.core.security.EncryptionManager
import com.sargmike228.core.security.SignalProtocolManagerImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MessagingService : Service() {
    
    @Inject
    lateinit var p2pManager: DirectSocketP2PManager
    
    @Inject
    lateinit var encryptionManager: EncryptionManager
    
    @Inject
    lateinit var signalManager: SignalProtocolManagerImpl
    
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    
    override fun onCreate() {
        super.onCreate()
        println("MessagingService created")
        // Start P2P server for receiving messages
        p2pManager.startServer(port = 5000)
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when (action) {
            ACTION_SEND_MESSAGE -> {
                val recipientId = intent.getStringExtra("recipientId") ?: return START_STICKY
                val messageText = intent.getStringExtra("message") ?: return START_STICKY
                sendMessage(recipientId, messageText)
            }
            ACTION_CONNECT -> {
                val host = intent.getStringExtra("host") ?: return START_STICKY
                val port = intent.getIntExtra("port", 5000)
                p2pManager.connectToServer(host, port)
            }
        }
        return START_STICKY
    }
    
    private fun sendMessage(recipientId: String, messageText: String) {
        serviceScope.launch {
            try {
                // Encrypt message using Signal Protocol
                val encryptedData = signalManager.encryptMessage(messageText, recipientId)
                
                // Send via P2P
                p2pManager.sendMessage(recipientId, encryptedData)
                println("Message sent to $recipientId")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        p2pManager.stopServer()
        serviceScope.cancel()
        println("MessagingService destroyed")
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    companion object {
        const val ACTION_SEND_MESSAGE = "com.sargmike228.SEND_MESSAGE"
        const val ACTION_CONNECT = "com.sargmike228.CONNECT"
    }
}