package com.ecustudent.roomdatabase

import android.app.Application

class CriminalIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        EventRepository.initialize(this)
    }
}