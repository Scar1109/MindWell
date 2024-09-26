package com.example.mindwell

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mindwell.databinding.ActivitySignupPageBinding
import com.google.firebase.auth.FirebaseAuth

class SignupPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignupPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Back button to close the activity
        binding.backBtn.setOnClickListener {
            finish()
        }

        // Signup button click listener
        binding.btnSignup.setOnClickListener {
            val email = binding.mailInput.text.toString().trim()
            val password = binding.pwdInput.text.toString().trim()

            // Validate input fields
            if (validateInput(email, password)) {
                registerUser(email, password)
            }
        }
    }

    // Validate input fields before sign-up
    private fun validateInput(email: String, password: String): Boolean {
        // Check if email is empty
        if (email.isEmpty()) {
            binding.mailInput.error = "Email is required"
            binding.mailInput.requestFocus()
            return false
        }

        // Check if email format is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.mailInput.error = "Please enter a valid email"
            binding.mailInput.requestFocus()
            return false
        }

        // Check if password is empty
        if (password.isEmpty()) {
            binding.pwdInput.error = "Password is required"
            binding.pwdInput.requestFocus()
            return false
        }

        // Check if password is at least 6 characters long (Firebase requirement)
        if (password.length < 6) {
            binding.pwdInput.error = "Password must be at least 6 characters"
            binding.pwdInput.requestFocus()
            return false
        }

        // Additional password complexity checks can be added here
        return true
    }

    // Register the user with Firebase
    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign-up successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomePage::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Sign-up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
