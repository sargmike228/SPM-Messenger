package com.sargmike228.spm_messenger

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SPMMessengerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        println("SPM Messenger Application started")
    }
}