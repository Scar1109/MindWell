package com.example.mindwell

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mindwell.databinding.ActivityHomePageBinding

class HomePage : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle navigation bar click events
        binding.navHome.setOnClickListener {
            // Already on the home page, show a message
            Toast.makeText(this, "You are already on Home", Toast.LENGTH_SHORT).show()
        }

        binding.navTherapy.setOnClickListener {
            startActivity(Intent(this, TherapyPage::class.java))
        }

        binding.navCommunity.setOnClickListener {
            startActivity(Intent(this, CommunityPage::class.java))
        }

        binding.navMood.setOnClickListener {
            startActivity(Intent(this, MoodPage::class.java))
        }

        binding.navProfile.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        // Handle buttons for "Mental Health" and "Mental Health Tips"
        binding.btnMentalHealth.setOnClickListener {
            startActivity(Intent(this, MentalHealthDetails::class.java))
        }

        binding.btnMentalHealthTips.setOnClickListener {
            startActivity(Intent(this, MentalHealthTips::class.java))
        }
    }
}
