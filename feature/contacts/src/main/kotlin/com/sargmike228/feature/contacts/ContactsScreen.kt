package com.sargmike228.feature.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ContactUI(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val avatarColor: Color,
    val isVerified: Boolean,
    val status: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen() {
    val contacts = remember {
        mutableStateOf(listOf(
            ContactUI("1", "Alice Johnson", "+1 (555) 123-4567", Color(0xFF6366F1), true, "Online"),
            ContactUI("2", "Bob Smith", "+1 (555) 234-5678", Color(0xFFEC4899), true, "Offline"),
            ContactUI("3", "Charlie Brown", "+1 (555) 345-6789", Color(0xFF8B5CF6), false, "Online"),
            ContactUI("4", "Diana Prince", "+1 (555) 456-7890", Color(0xFF10B981), true, "Offline"),
            ContactUI("5", "Edward Norton", "+1 (555) 567-8901", Color(0xFFF59E0B), true, "Online"),
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
            title = { Text("Контакты", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
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
            placeholder = { Text("Поиск контактов...") },
            leadingIcon = { Icon(Icons.Default.Search, "Search") },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            )
        )

        // Contacts List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            items(contacts.value) { contact ->
                ContactListItem(contact = contact)
            }
        }
    }
}

@Composable
fun ContactListItem(contact: ContactUI) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(contact.avatarColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                contact.name.take(2),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Contact Info
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    contact.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (contact.isVerified) {
                    Icon(
                        Icons.Default.Message,
                        "Verified",
                        modifier = Modifier.size(14.dp),
                        tint = Color(0xFF10B981)
                    )
                }
            }
            Text(
                contact.phoneNumber,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                contact.status,
                fontSize = 11.sp,
                color = if (contact.status == "Online") Color.Green else Color.Gray
            )
        }

        // Action Buttons
        IconButton(
            onClick = { },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(Icons.Default.Call, "Call", tint = MaterialTheme.colorScheme.primary)
        }

        IconButton(
            onClick = { },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(Icons.Default.Message, "Message", tint = MaterialTheme.colorScheme.primary)
        }
    }
    Divider(modifier = Modifier.padding(horizontal = 8.dp))
}