package com.sargmike228.feature.calls

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class CallLogEntry(
    val id: String,
    val contactName: String,
    val phoneNumber: String,
    val timestamp: Long,
    val duration: Long,
    val type: String, // INCOMING, OUTGOING, MISSED
    val callType: String // VOICE, VIDEO
)

class CallLogViewModel @Inject constructor() : ViewModel() {
    
    private val _callLog = MutableStateFlow<List<CallLogEntry>>(emptyList())
    val callLog: StateFlow<List<CallLogEntry>> = _callLog
    
    private val _filteredCallLog = MutableStateFlow<List<CallLogEntry>>(emptyList())
    val filteredCallLog: StateFlow<List<CallLogEntry>> = _filteredCallLog
    
    fun addCallEntry(
        contactName: String,
        phoneNumber: String,
        duration: Long,
        type: String,
        callType: String
    ) {
        val newEntry = CallLogEntry(
            id = java.util.UUID.randomUUID().toString(),
            contactName = contactName,
            phoneNumber = phoneNumber,
            timestamp = System.currentTimeMillis(),
            duration = duration,
            type = type,
            callType = callType
        )
        
        val currentLog = _callLog.value.toMutableList()
        currentLog.add(0, newEntry)
        _callLog.value = currentLog
    }
    
    fun filterCallLog(query: String) {
        val filtered = if (query.isBlank()) {
            _callLog.value
        } else {
            _callLog.value.filter {
                it.contactName.contains(query, ignoreCase = true) ||
                it.phoneNumber.contains(query)
            }
        }
        _filteredCallLog.value = filtered
    }
    
    fun deleteCallEntry(id: String) {
        val currentLog = _callLog.value.filter { it.id != id }
        _callLog.value = currentLog
    }
    
    fun clearCallLog() {
        _callLog.value = emptyList()
        _filteredCallLog.value = emptyList()
    }
    
    fun getCallLogByDate(date: Long): List<CallLogEntry> {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        val startTime = calendar.timeInMillis
        
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 23)
        calendar.set(java.util.Calendar.MINUTE, 59)
        calendar.set(java.util.Calendar.SECOND, 59)
        val endTime = calendar.timeInMillis
        
        return _callLog.value.filter {
            it.timestamp in startTime..endTime
        }
    }
}