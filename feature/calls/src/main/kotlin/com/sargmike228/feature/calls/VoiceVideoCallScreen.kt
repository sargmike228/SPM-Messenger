package com.sargmike228.feature.calls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material.icons.filled.SpeakerOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VoiceCallScreen(
    contactName: String,
    onEndCall: () -> Unit
) {
    val callDuration = remember { mutableStateOf(0L) }
    val isMuted = remember { mutableStateOf(false) }
    val isSpeaker = remember { mutableStateOf(false) }

    // Timer for call duration
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            callDuration.value++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Contact Info
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        contactName[0].toString(),
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    contactName,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    formatCallDuration(callDuration.value),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Control Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mute Button
            FloatingActionButton(
                onClick = { isMuted.value = !isMuted.value },
                modifier = Modifier.size(56.dp),
                containerColor = if (isMuted.value) Color.Red else MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    if (isMuted.value) Icons.Default.MicOff else Icons.Default.Mic,
                    "Mute",
                    tint = if (isMuted.value) Color.White else MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            // Speaker Button
            FloatingActionButton(
                onClick = { isSpeaker.value = !isSpeaker.value },
                modifier = Modifier.size(56.dp),
                containerColor = if (isSpeaker.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    if (isSpeaker.value) Icons.Default.Speaker else Icons.Default.SpeakerOff,
                    "Speaker"
                )
            }

            // End Call Button
            FloatingActionButton(
                onClick = onEndCall,
                modifier = Modifier.size(56.dp),
                containerColor = Color.Red
            ) {
                Icon(
                    Icons.Default.CallEnd,
                    "End Call",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun VideoCallScreen(
    contactName: String,
    onEndCall: () -> Unit
) {
    val callDuration = remember { mutableStateOf(0L) }
    val isCameraOn = remember { mutableStateOf(true) }
    val isMuted = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            callDuration.value++
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Video Placeholder
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Video Stream",
                color = Color.White,
                fontSize = 24.sp
            )
        }

        // Top Info Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    Color.Black.copy(alpha = 0.5f),
                    RoundedCornerShape(8.dp)
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                contactName,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                formatCallDuration(callDuration.value),
                color = Color.White,
                fontSize = 14.sp
            )
        }

        // Control Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingActionButton(
                onClick = { isCameraOn.value = !isCameraOn.value },
                modifier = Modifier.size(56.dp),
                containerColor = if (isCameraOn.value) MaterialTheme.colorScheme.primary else Color.Red
            ) {
                Icon(
                    if (isCameraOn.value) Icons.Default.Videocam else Icons.Default.VideocamOff,
                    "Camera"
                )
            }

            FloatingActionButton(
                onClick = { isMuted.value = !isMuted.value },
                modifier = Modifier.size(56.dp),
                containerColor = if (isMuted.value) Color.Red else MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    if (isMuted.value) Icons.Default.MicOff else Icons.Default.Mic,
                    "Mute"
                )
            }

            FloatingActionButton(
                onClick = onEndCall,
                modifier = Modifier.size(56.dp),
                containerColor = Color.Red
            ) {
                Icon(
                    Icons.Default.CallEnd,
                    "End Call",
                    tint = Color.White
                )
            }
        }
    }
}