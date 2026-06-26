package com.sargmike228.feature.messaging

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
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
import com.sargmike228.feature.messaging.repository.Group

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsScreen() {
    val groups = remember {
        mutableStateOf(listOf(
            Group(
                "1",
                "Друзья",
                listOf("Alice", "Bob", "Charlie"),
                Color(0xFF6366F1),
                System.currentTimeMillis(),
                "currentUser"
            ),
            Group(
                "2",
                "Работа",
                listOf("Diana", "Edward", "Frank"),
                Color(0xFFEC4899),
                System.currentTimeMillis(),
                "currentUser"
            )
        ))
    }
    val searchQuery = remember { mutableStateOf("") }
    val showCreateDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Группы", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = { showCreateDialog.value = true }) {
                    Icon(Icons.Default.Add, "Create Group")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp)),
            placeholder = { Text("Поиск групп...") },
            leadingIcon = { Icon(Icons.Default.Search, "Search") },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            items(groups.value) { group ->
                GroupListItem(group)
            }
        }
    }

    if (showCreateDialog.value) {
        CreateGroupDialog(
            onDismiss = { showCreateDialog.value = false },
            onCreateGroup = { name, members ->
                showCreateDialog.value = false
            }
        )
    }
}

@Composable
fun GroupListItem(group: Group) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(group.avatarColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                group.name.take(2),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                group.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "${group.members.size} участников",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
    Divider(modifier = Modifier.padding(horizontal = 8.dp))
}

@Composable
fun CreateGroupDialog(
    onDismiss: () -> Unit,
    onCreateGroup: (String, List<String>) -> Unit
) {
    val groupName = remember { mutableStateOf("") }
    val selectedMembers = remember { mutableStateOf(setOf<String>()) }
    val availableMembers = listOf("Alice", "Bob", "Charlie", "Diana", "Edward")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Создать группу") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = groupName.value,
                    onValueChange = { groupName.value = it },
                    label = { Text("Название группы") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Text("Выберите участников:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {
                    items(availableMembers) { member ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val current = selectedMembers.value.toMutableSet()
                                    if (current.contains(member)) {
                                        current.remove(member)
                                    } else {
                                        current.add(member)
                                    }
                                    selectedMembers.value = current.toSet()
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedMembers.value.contains(member),
                                onCheckedChange = {
                                    val current = selectedMembers.value.toMutableSet()
                                    if (it) current.add(member) else current.remove(member)
                                    selectedMembers.value = current.toSet()
                                }
                            )
                            Text(member, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (groupName.value.isNotBlank() && selectedMembers.value.isNotEmpty()) {
                        onCreateGroup(groupName.value, selectedMembers.value.toList())
                    }
                }
            ) {
                Text("Создать")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}