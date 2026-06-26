package com.sargmike228.feature.messaging.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class Group(
    val id: String,
    val name: String,
    val members: List<String>,
    val avatarColor: androidx.compose.ui.graphics.Color,
    val createdAt: Long,
    val admin: String
)

@Singleton
class GroupRepository @Inject constructor(
    private val messagingRepository: MessagingRepository
) {
    
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups
    
    suspend fun createGroup(
        name: String,
        members: List<String>,
        adminId: String
    ): Group {
        val groupId = java.util.UUID.randomUUID().toString()
        val newGroup = Group(
            id = groupId,
            name = name,
            members = members,
            avatarColor = generateColorForGroup(name),
            createdAt = System.currentTimeMillis(),
            admin = adminId
        )
        
        val currentGroups = _groups.value.toMutableList()
        currentGroups.add(newGroup)
        _groups.emit(currentGroups.toList())
        
        return newGroup
    }
    
    suspend fun addMemberToGroup(groupId: String, memberId: String) {
        val group = _groups.value.find { it.id == groupId } ?: return
        val updatedMembers = group.members + memberId
        val updatedGroup = group.copy(members = updatedMembers)
        
        val currentGroups = _groups.value.toMutableList()
        val index = currentGroups.indexOfFirst { it.id == groupId }
        if (index != -1) {
            currentGroups[index] = updatedGroup
            _groups.emit(currentGroups.toList())
        }
    }
    
    suspend fun removeMemberFromGroup(groupId: String, memberId: String) {
        val group = _groups.value.find { it.id == groupId } ?: return
        val updatedMembers = group.members.filter { it != memberId }
        val updatedGroup = group.copy(members = updatedMembers)
        
        val currentGroups = _groups.value.toMutableList()
        val index = currentGroups.indexOfFirst { it.id == groupId }
        if (index != -1) {
            currentGroups[index] = updatedGroup
            _groups.emit(currentGroups.toList())
        }
    }
    
    suspend fun deleteGroup(groupId: String) {
        val currentGroups = _groups.value.filter { it.id != groupId }
        _groups.emit(currentGroups)
    }
    
    suspend fun sendGroupMessage(
        groupId: String,
        senderId: String,
        content: String
    ) {
        // Send message to all members in the group
        val group = _groups.value.find { it.id == groupId } ?: return
        group.members.forEach { memberId ->
            if (memberId != senderId) {
                messagingRepository.sendMessage(
                    conversationId = groupId,
                    recipientId = memberId,
                    content = content
                )
            }
        }
    }
    
    private fun generateColorForGroup(name: String): androidx.compose.ui.graphics.Color {
        val colors = listOf(
            androidx.compose.ui.graphics.Color(0xFF6366F1),
            androidx.compose.ui.graphics.Color(0xFFEC4899),
            androidx.compose.ui.graphics.Color(0xFF8B5CF6),
            androidx.compose.ui.graphics.Color(0xFF10B981),
            androidx.compose.ui.graphics.Color(0xFFF59E0B)
        )
        return colors[name.hashCode() % colors.size]
    }
}