package com.example.mindwell

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mindwell.databinding.ActivityLoginSignupPageBinding
import com.google.firebase.auth.FirebaseAuth

class LoginSignupPage : AppCompatActivity() {
    private lateinit var binding: ActivityLoginSignupPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate the layout using ViewBinding
        binding = ActivityLoginSignupPageBinding.inflate(layoutInflater)
        setContentView(binding.root) // Set the view using binding

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up button click listeners
        binding.loginBtn.setOnClickListener {
            // Navigate to the login page
            startActivity(Intent(this, LoginPage::class.java))
        }

        binding.signupBtn.setOnClickListener {
            // Navigate to the signup page
            startActivity(Intent(this, SignupPage::class.java))
        }
    }
}
