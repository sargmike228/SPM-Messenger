package com.sargmike228.core.network

import kotlinx.coroutines.*
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirectSocketP2PManager @Inject constructor() {
    
    private var serverSocket: ServerSocket? = null
    private val clientConnections = mutableMapOf<String, Socket>()
    private val scope = CoroutineScope(Dispatchers.IO + Job())
    
    fun startServer(port: Int = 5000) {
        scope.launch {
            try {
                serverSocket = ServerSocket(port)
                println("Server started on port $port")
                
                while (isActive) {
                    val clientSocket = serverSocket?.accept()
                    if (clientSocket != null) {
                        val clientId = "${clientSocket.inetAddress.hostAddress}:${clientSocket.port}"
                        clientConnections[clientId] = clientSocket
                        println("Client connected: $clientId")
                        
                        // Handle client in separate coroutine
                        handleClient(clientId, clientSocket)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun connectToServer(host: String, port: Int = 5000): Boolean {
        return try {
            val socket = Socket(host, port)
            val clientId = "$host:$port"
            clientConnections[clientId] = socket
            println("Connected to server: $clientId")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    fun sendMessage(peerId: String, message: ByteArray) {
        try {
            val socket = clientConnections[peerId]
            val output = socket?.getOutputStream()
            output?.write(message.size)
            output?.write(message)
            output?.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun receiveMessage(peerId: String): ByteArray? {
        return try {
            val socket = clientConnections[peerId]
            val input = socket?.getInputStream()
            val size = input?.read() ?: return null
            val buffer = ByteArray(size)
            input?.read(buffer)
            buffer
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    fun disconnectPeer(peerId: String) {
        try {
            clientConnections[peerId]?.close()
            clientConnections.remove(peerId)
            println("Disconnected from peer: $peerId")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun stopServer() {
        try {
            serverSocket?.close()
            clientConnections.values.forEach { it.close() }
            clientConnections.clear()
            scope.cancel()
            println("Server stopped")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun handleClient(clientId: String, socket: Socket) {
        scope.launch {
            try {
                val input = socket.getInputStream()
                val output = socket.getOutputStream()
                val buffer = ByteArray(8192)
                
                while (isActive) {
                    val bytes = input.read(buffer)
                    if (bytes > 0) {
                        // Process received data
                        println("Received $bytes bytes from $clientId")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                disconnectPeer(clientId)
            }
        }
    }
}