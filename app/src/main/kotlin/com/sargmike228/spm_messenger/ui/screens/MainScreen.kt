package com.sargmike228.spm_messenger.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController) {
    val selectedTab = remember { mutableStateOf(0) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab.value) {
            Tab(
                selected = selectedTab.value == 0,
                onClick = { selectedTab.value = 0 },
                text = { Text("Сообщения") }
            )
            Tab(
                selected = selectedTab.value == 1,
                onClick = { selectedTab.value = 1 },
                text = { Text("Контакты") }
            )
            Tab(
                selected = selectedTab.value == 2,
                onClick = { selectedTab.value = 2 },
                text = { Text("Звонки") }
            )
            Tab(
                selected = selectedTab.value == 3,
                onClick = { selectedTab.value = 3 },
                text = { Text("Настройки") }
            )
        }
        
        when (selectedTab.value) {
            0 -> MessagingScreen()
            1 -> ContactsScreen()
            2 -> CallsScreen()
            3 -> SettingsScreen()
        }
    }
}

@Composable
fun MessagingScreen() {
    Text("Messaging Screen")
}

@Composable
fun ContactsScreen() {
    Text("Contacts Screen")
}

@Composable
fun CallsScreen() {
    Text("Calls Screen")
}

@Composable
fun SettingsScreen() {
    Text("Settings Screen")
}