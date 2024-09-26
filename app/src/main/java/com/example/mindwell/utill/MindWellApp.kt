package com.example.mindwell

import android.app.Application
import com.google.firebase.FirebaseApp

class MindWellApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
    }
}
