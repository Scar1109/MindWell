package com.example.mindwell

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mindwell.databinding.ActivitySignupPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivitySignupPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Handle Signup button click
        binding.btnSignup.setOnClickListener {
            val email = binding.mailInput.text.toString().trim()
            val password = binding.pwdInput.text.toString().trim()
            val username = binding.usernameInput.text.toString().trim() // Get the username

            if (validateInput(email, password, username)) {
                registerUser(email, password, username)
            }
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    // Validate input fields before sign-up
    private fun validateInput(email: String, password: String, username: String): Boolean {
        if (email.isEmpty()) {
            binding.mailInput.error = "Email is required"
            binding.mailInput.requestFocus()
            return false
        }

        if (password.isEmpty() || password.length < 6) {
            binding.pwdInput.error = "Password must be at least 6 characters"
            binding.pwdInput.requestFocus()
            return false
        }

        if (username.isEmpty()) {
            binding.usernameInput.error = "Username is required"
            binding.usernameInput.requestFocus()
            return false
        }

        return true
    }

    // Register the user with Firebase Auth and store username in Firestore
    private fun registerUser(email: String, password: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        val uid = user.uid

                        // Save user data to Firestore
                        val userData = hashMapOf(
                            "email" to email,
                            "username" to username,
                            "userType" to "User"
                        )

                        db.collection("users").document(uid).set(userData)
                            .addOnCompleteListener { firestoreTask ->
                                if (firestoreTask.isSuccessful) {
                                    Toast.makeText(this, "Sign-up successful", Toast.LENGTH_SHORT).show()
                                    // Navigate to home page
                                    startActivity(Intent(this, HomePage::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this, "Failed to save user data: ${firestoreTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this, "Sign-up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
