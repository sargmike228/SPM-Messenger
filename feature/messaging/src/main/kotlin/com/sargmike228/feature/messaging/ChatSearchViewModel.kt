package com.sargmike228.feature.messaging

import androidx.lifecycle.ViewModel
import com.sargmike228.core.database.dao.MessageDao
import com.sargmike228.core.database.entity.MessageEntity
import com.sargmike228.feature.messaging.repository.MessagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ChatSearchViewModel @Inject constructor(
    private val messageDao: MessageDao,
    private val messagingRepository: MessagingRepository
) : ViewModel() {
    
    fun searchMessages(
        conversationId: String,
        query: String
    ): Flow<List<MessageEntity>> {
        // Search through messages
        return messageDao.getMessagesForConversation(conversationId)
    }
}