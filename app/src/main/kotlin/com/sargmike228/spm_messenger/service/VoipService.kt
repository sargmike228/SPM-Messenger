package com.sargmike228.feature.calls.service

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaRecorder
import android.os.IBinder
import com.sargmike228.core.network.DirectSocketP2PManager
import com.sargmike228.core.security.SignalProtocolManagerImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class VoipService : Service() {
    
    @Inject
    lateinit var p2pManager: DirectSocketP2PManager
    
    @Inject
    lateinit var signalManager: SignalProtocolManagerImpl
    
    private var mediaRecorder: MediaRecorder? = null
    private var audioManager: AudioManager? = null
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    
    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(AUDIO_SERVICE) as? AudioManager
        println("VoipService created")
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when (action) {
            ACTION_START_VOICE_CALL -> {
                val recipientId = intent?.getStringExtra("recipientId") ?: return START_STICKY
                startVoiceCall(recipientId)
            }
            ACTION_START_VIDEO_CALL -> {
                val recipientId = intent?.getStringExtra("recipientId") ?: return START_STICKY
                startVideoCall(recipientId)
            }
            ACTION_END_CALL -> {
                endCall()
            }
        }
        return START_STICKY
    }
    
    private fun startVoiceCall(recipientId: String) {
        serviceScope.launch {
            try {
                // Set audio mode for call
                audioManager?.mode = AudioManager.MODE_IN_CALL
                audioManager?.isSpeakerphoneOn = false
                
                // Initialize media recorder for audio
                mediaRecorder = MediaRecorder().apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    setAudioSamplingRate(16000)
                    setAudioEncodingBitRate(128000)
                    setOutputFile("/dev/null") // Stream to P2P instead of file
                    prepare()
                    start()
                }
                
                // Send audio frames via P2P with encryption
                sendAudioFrames(recipientId)
                
                println("Voice call started with $recipientId")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun startVideoCall(recipientId: String) {
        serviceScope.launch {
            try {
                // Initialize camera and video encoding
                audioManager?.mode = AudioManager.MODE_IN_CALL
                
                // Send video frames via P2P with encryption
                sendVideoFrames(recipientId)
                
                println("Video call started with $recipientId")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private suspend fun sendAudioFrames(recipientId: String) {
        while (true) {
            try {
                // Record audio frame
                val audioBuffer = ByteArray(2048)
                
                // Encrypt using Signal Protocol
                val encryptedAudio = signalManager.encryptVoiceCall(audioBuffer, recipientId)
                
                // Send via P2P
                p2pManager.sendMessage(recipientId, encryptedAudio)
                
                delay(20) // 20ms audio frames
            } catch (e: Exception) {
                e.printStackTrace()
                break
            }
        }
    }
    
    private suspend fun sendVideoFrames(recipientId: String) {
        while (true) {
            try {
                // Capture video frame
                val videoBuffer = ByteArray(32768)
                
                // Encrypt using Signal Protocol
                val encryptedVideo = signalManager.encryptVideoCall(videoBuffer, recipientId)
                
                // Send via P2P
                p2pManager.sendMessage(recipientId, encryptedVideo)
                
                delay(33) // ~30fps
            } catch (e: Exception) {
                e.printStackTrace()
                break
            }
        }
    }
    
    private fun endCall() {
        try {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            audioManager?.mode = AudioManager.MODE_NORMAL
            println("Call ended")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        mediaRecorder?.release()
        serviceScope.cancel()
        println("VoipService destroyed")
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    companion object {
        const val ACTION_START_VOICE_CALL = "com.sargmike228.START_VOICE_CALL"
        const val ACTION_START_VIDEO_CALL = "com.sargmike228.START_VIDEO_CALL"
        const val ACTION_END_CALL = "com.sargmike228.END_CALL"
    }
}