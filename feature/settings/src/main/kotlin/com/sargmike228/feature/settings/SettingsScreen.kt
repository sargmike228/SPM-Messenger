package com.sargmike228.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Bell
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SettingItemUI(
    val title: String,
    val description: String = "",
    val icon: ImageVector,
    val action: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val isDarkMode = remember { mutableStateOf(false) }
    val userName = remember { mutableStateOf("Сергей Миронов") }
    val userPhone = remember { mutableStateOf("+7 (999) 123-45-67") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        TopAppBar(
            title = { Text("Настройки", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Profile Section
            item {
                Text(
                    "Профиль",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }

            item {
                ProfileCard(
                    name = userName.value,
                    phone = userPhone.value,
                    onEdit = { }
                )
            }

            // Security Section
            item {
                Text(
                    "Безопасность",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }

            item {
                SettingItem(
                    title = "Приватность",
                    description = "Управление приватностью и видимостью",
                    icon = Icons.Default.Lock,
                    onClick = { }
                )
            }

            item {
                SettingItem(
                    title = "Блокировка экрана",
                    description = "Требовать отпечаток пальца",
                    icon = Icons.Default.Lock,
                    onClick = { }
                )
            }

            // Notifications Section
            item {
                Text(
                    "Уведомления",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }

            item {
                SettingItem(
                    title = "Звук уведомлений",
                    description = "Включить звук для новых сообщений",
                    icon = Icons.Default.Bell,
                    onClick = { }
                )
            }

            // Display Section
            item {
                Text(
                    "Отображение",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }

            item {
                SettingItemWithToggle(
                    title = "Тёмная тема",
                    description = "Использовать тёмный режим",
                    icon = Icons.Default.DarkMode,
                    isChecked = isDarkMode.value,
                    onToggle = { isDarkMode.value = it }
                )
            }

            // Danger Zone
            item {
                Text(
                    "Опасная зона",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            item {
                SettingItem(
                    title = "Очистить чаты",
                    description = "Удалить все сообщения и чаты",
                    icon = Icons.Default.Delete,
                    onClick = { },
                    isDestructive = true
                )
            }

            item {
                SettingItem(
                    title = "Выход",
                    description = "Выход из аккаунта",
                    icon = Icons.Default.Logout,
                    onClick = { },
                    isDestructive = true
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ProfileCard(
    name: String,
    phone: String,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    name.take(2),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    phone,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Edit Button
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, "Edit")
            }
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    description: String = "",
    icon: ImageVector,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            icon,
            title,
            modifier = Modifier.size(24.dp),
            tint = if (isDestructive) Color.Red else MaterialTheme.colorScheme.primary
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDestructive) Color.Red else MaterialTheme.colorScheme.onBackground
            )
            if (description.isNotEmpty()) {
                Text(
                    description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Icon(
            Icons.Default.ArrowForwardIos,
            "Forward",
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    Divider(modifier = Modifier.padding(horizontal = 16.dp))
}

@Composable
fun SettingItemWithToggle(
    title: String,
    description: String = "",
    icon: ImageVector,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!isChecked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            icon,
            title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            if (description.isNotEmpty()) {
                Text(
                    description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onToggle
        )
    }
    Divider(modifier = Modifier.padding(horizontal = 16.dp))
}