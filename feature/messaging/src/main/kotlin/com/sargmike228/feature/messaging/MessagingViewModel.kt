package com.sargmike228.feature.messaging

import androidx.lifecycle.ViewModel
import com.sargmike228.core.database.entity.MessageEntity
import com.sargmike228.core.network.P2PConnectionManager
import com.sargmike228.core.security.SignalProtocolManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MessagingViewModel @Inject constructor(
    private val p2pManager: P2PConnectionManager,
    private val signalManager: SignalProtocolManager
) : ViewModel() {
    
    private val _messages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val messages = _messages.asStateFlow()
    
    fun sendMessage(recipientId: String, content: String) {
        // Encrypt message using Signal Protocol
        val encryptedContent = signalManager.encryptMessage(content, recipientId)
        
        // Send via P2P connection
        p2pManager.sendMessageToPeer(recipientId, encryptedContent)
    }
    
    fun receiveMessage(senderId: String, encryptedContent: ByteArray) {
        // Decrypt message
        val decryptedContent = signalManager.decryptMessage(encryptedContent, senderId)
        
        // Add to messages list
    }
}