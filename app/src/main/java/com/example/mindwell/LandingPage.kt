package com.example.mindwell

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LandingPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_landing_page)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if user is already logged in
        val currentUser = auth.currentUser

        // Use a handler to simulate a delay (for splash effect)
        Handler(Looper.getMainLooper()).postDelayed({
            if (currentUser != null) {
                // User is logged in, redirect to HomePage
                startActivity(Intent(this, HomePage::class.java))
                finish()
            } else {
                // No user is logged in, redirect to LoginSignupPage
                startActivity(Intent(this, LoginSignupPage::class.java))
                finish()
            }
        }, 2500L)  // Delay for 2.5 seconds (optional for splash screen effect)
    }
}
