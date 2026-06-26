package com.sargmike228.feature.messaging

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

data class MessageUI(
    val id: String,
    val content: String,
    val timestamp: Long,
    val isSent: Boolean,
    val messageType: MessageType = MessageType.TEXT
)

enum class MessageType {
    TEXT, VOICE, IMAGE, FILE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    contactName: String,
    isOnline: Boolean,
    onBack: () -> Unit,
    onCall: () -> Unit,
    onVideoCall: () -> Unit
) {
    val messages = remember {
        mutableStateOf(listOf(
            MessageUI("1", "Привет!", System.currentTimeMillis() - 600000, false),
            MessageUI("2", "Привет! Как дела?", System.currentTimeMillis() - 580000, true),
            MessageUI("3", "Отлично! А у тебя?", System.currentTimeMillis() - 500000, false),
            MessageUI("4", "Тоже хорошо 😊", System.currentTimeMillis() - 480000, true),
        ))
    }
    val messageInput = remember { mutableStateOf("") }
    val isRecording = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Column {
                    Text(contactName, fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    Text(
                        if (isOnline) "Online" else "Offline",
                        fontSize = 12.sp,
                        color = if (isOnline) Color.Green else Color.Gray
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            },
            actions = {
                IconButton(onClick = onCall) {
                    Icon(Icons.Default.Call, "Call")
                }
                IconButton(onClick = onVideoCall) {
                    Icon(Icons.Default.Videocam, "Video Call")
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.MoreVert, "More")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            reverseLayout = true
        ) {
            items(messages.value.reversed()) { message ->
                MessageBubble(message)
            }
        }

        Divider()

        // Input Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = { }) {
                Icon(Icons.Default.AttachFile, "Attach")
            }

            OutlinedTextField(
                value = messageInput.value,
                onValueChange = { messageInput.value = it },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp, max = 100.dp),
                placeholder = { Text("Сообщение...") },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            if (!isRecording.value) {
                IconButton(onClick = { isRecording.value = true }) {
                    Icon(Icons.Default.Mic, "Voice")
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Send, "Send")
                }
            } else {
                Button(
                    onClick = { isRecording.value = false },
                    modifier = Modifier.height(40.dp)
                ) {
                    Text("Stop")
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: MessageUI) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isSent) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(RoundedCornerShape(16.dp)),
            color = if (message.isSent) Color(0xFF0084FF) else Color(0xFFE5E5EA),
            tonalElevation = 4.dp
        ) {
            when (message.messageType) {
                MessageType.TEXT -> {
                    Text(
                        message.content,
                        modifier = Modifier.padding(12.dp),
                        color = if (message.isSent) Color.White else Color.Black,
                        fontSize = 14.sp
                    )
                }
                MessageType.VOICE -> {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Mic, "Voice", modifier = Modifier.size(20.dp))
                        Text(
                            "Voice Message 0:45",
                            color = if (message.isSent) Color.White else Color.Black
                        )
                    }
                }
                else -> {
                    Text("Attachment", modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}