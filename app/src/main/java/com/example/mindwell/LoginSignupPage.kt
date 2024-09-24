package com.example.mindwell

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mindwell.databinding.ActivityLoginSignupPageBinding

private lateinit var binding: ActivityLoginSignupPageBinding

class LoginSignupPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSignupPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_login_signup_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
            finish()
        }

        binding.signupBtn.setOnClickListener {
            startActivity(Intent(this, SignupPage::class.java))
            finish()
        }
    }
}