package com.example.mindwell

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LandingPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_landing_page)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Check if user is already logged in
        val currentUser = auth.currentUser

        // Use a handler to simulate a delay (for splash effect)
        Handler(Looper.getMainLooper()).postDelayed({
            if (currentUser != null) {
                // User is logged in, check user type
                checkUserTypeAndRedirect(currentUser.uid)
            } else {
                // No user is logged in, redirect to LoginSignupPage
                startActivity(Intent(this, LoginSignupPage::class.java))
                finish()
            }
        }, 2500L)  // Delay for 2.5 seconds (optional for splash screen effect)
    }

    private fun checkUserTypeAndRedirect(userId: String) {
        // Fetch the userType from Firestore
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userType = document.getString("userType")
                    if (userType == "Doctor") {
                        // Redirect to Doctor Reply Page
                        startActivity(Intent(this, DoctorUserListPage::class.java))
                    } else {
                        // Redirect to Home Page for regular users
                        startActivity(Intent(this, HomePage::class.java))
                    }
                    finish() // Close LandingPage
                } else {
                    Toast.makeText(this, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginSignupPage::class.java))
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error fetching user data: ${it.message}", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginSignupPage::class.java))
                finish()
            }
    }
}
