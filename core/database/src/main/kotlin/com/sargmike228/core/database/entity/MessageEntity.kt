package com.sargmike228.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val conversationId: String,
    val senderId: String,
    val recipientId: String,
    val content: String, // Encrypted
    val timestamp: Long,
    val isRead: Boolean = false,
    val messageType: MessageType = MessageType.TEXT,
    val attachmentPath: String? = null
)

enum class MessageType {
    TEXT,
    VOICE,
    IMAGE,
    VIDEO,
    FILE,
    LOCATION
}