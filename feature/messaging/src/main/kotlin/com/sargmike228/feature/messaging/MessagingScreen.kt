package com.sargmike228.spm_messenger.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sargmike228.feature.messaging.MessagingViewModel
import java.text.SimpleDateFormat
import java.util.*

data class ChatItemUI(
    val id: String,
    val contactName: String,
    val lastMessage: String,
    val timestamp: Long,
    val unreadCount: Int,
    val avatarColor: Color,
    val isOnline: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagingScreen(
    navController: NavController,
    viewModel: MessagingViewModel = hiltViewModel()
) {
    val chats = remember {
        mutableStateOf(listOf(
            ChatItemUI("1", "Alice", "Привет! Как дела?", System.currentTimeMillis() - 300000, 2, Color(0xFF6366F1), true),
            ChatItemUI("2", "Bob", "Спасибо, отлично! 😊", System.currentTimeMillis() - 3600000, 0, Color(0xFFEC4899), false),
            ChatItemUI("3", "Charlie", "Ок, увидимся завтра", System.currentTimeMillis() - 7200000, 1, Color(0xFF8B5CF6), true),
        ))
    }
    val searchQuery = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        TopAppBar(
            title = { Text("Сообщения", fontSize = 24.sp) },
            actions = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Search, "Search")
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Add, "Add")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp)),
            placeholder = { Text("Поиск...") },
            leadingIcon = { Icon(Icons.Default.Search, "Search") },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            )
        )

        // Chat List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            items(chats.value) { chat ->
                ChatListItem(
                    chat = chat,
                    onClick = {
                        navController.navigate("chat/${chat.id}")
                    }
                )
            }
        }
    }
}

@Composable
fun ChatListItem(
    chat: ChatItemUI,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(chat.avatarColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                chat.contactName[0].toString(),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            if (chat.isOnline) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                        .align(Alignment.BottomEnd)
                        .offset((-4).dp, (-4).dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Chat Info
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    chat.contactName,
                    fontSize = 16.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    formatTime(chat.timestamp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                chat.lastMessage,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (chat.unreadCount > 0) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    chat.unreadCount.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }
        }
    }
    Divider(modifier = Modifier.padding(horizontal = 8.dp))
}

private fun formatTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60000 -> "Сейчас"
        diff < 3600000 -> "${diff / 60000}м"
        diff < 86400000 -> "${diff / 3600000}ч"
        else -> SimpleDateFormat("dd/MM", Locale("ru")).format(Date(timestamp))
    }
}