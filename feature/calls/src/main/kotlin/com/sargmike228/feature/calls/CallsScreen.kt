package com.sargmike228.feature.calls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

data class CallHistoryUI(
    val id: String,
    val contactName: String,
    val timestamp: Long,
    val duration: Long, // in seconds
    val callType: CallType,
    val direction: CallDirection,
    val avatarColor: Color
)

enum class CallType {
    VOICE, VIDEO
}

enum class CallDirection {
    INCOMING, OUTGOING, MISSED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallsScreen() {
    val callHistory = remember {
        mutableStateOf(listOf(
            CallHistoryUI("1", "Alice", System.currentTimeMillis() - 1800000, 1200, CallType.VIDEO, CallDirection.OUTGOING, Color(0xFF6366F1)),
            CallHistoryUI("2", "Bob", System.currentTimeMillis() - 3600000, 600, CallType.VOICE, CallDirection.INCOMING, Color(0xFFEC4899)),
            CallHistoryUI("3", "Charlie", System.currentTimeMillis() - 5400000, 0, CallType.VOICE, CallDirection.MISSED, Color(0xFF8B5CF6)),
            CallHistoryUI("4", "Diana", System.currentTimeMillis() - 7200000, 1800, CallType.VIDEO, CallDirection.INCOMING, Color(0xFF10B981)),
            CallHistoryUI("5", "Edward", System.currentTimeMillis() - 10800000, 900, CallType.VOICE, CallDirection.OUTGOING, Color(0xFFF59E0B)),
        ))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        TopAppBar(
            title = { Text("Звонки", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        // Call History List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            items(callHistory.value) { call ->
                CallHistoryItem(call)
            }
        }
    }
}

@Composable
fun CallHistoryItem(call: CallHistoryUI) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(call.avatarColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                call.contactName[0].toString(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Call Info
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    call.contactName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    when (call.callType) {
                        CallType.VOICE -> Icons.Default.Call
                        CallType.VIDEO -> Icons.Default.Videocam
                    },
                    "Call Type",
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    when (call.direction) {
                        CallDirection.INCOMING -> Icons.Default.CallReceived
                        CallDirection.OUTGOING -> Icons.Default.CallMade
                        CallDirection.MISSED -> Icons.Default.CallMissed
                    },
                    "Direction",
                    modifier = Modifier.size(14.dp),
                    tint = when (call.direction) {
                        CallDirection.MISSED -> Color.Red
                        CallDirection.INCOMING -> Color.Green
                        CallDirection.OUTGOING -> Color.Blue
                    }
                )
                Text(
                    if (call.duration > 0) formatCallDuration(call.duration) else "Не ответил",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    formatDate(call.timestamp),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Call Button
        FloatingActionButton(
            onClick = { },
            modifier = Modifier.size(44.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                if (call.callType == CallType.VIDEO) Icons.Default.Videocam else Icons.Default.Call,
                "Call"
            )
        }
    }
    Divider(modifier = Modifier.padding(horizontal = 8.dp))
}

private fun formatCallDuration(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return when {
        hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, secs)
        else -> String.format("%02d:%02d", minutes, secs)
    }
}

private fun formatDate(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 86400000 -> SimpleDateFormat("HH:mm", Locale("ru")).format(Date(timestamp))
        else -> SimpleDateFormat("dd/MM", Locale("ru")).format(Date(timestamp))
    }
}