package com.example.mindwell

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mindwell.databinding.ActivityCommunityPageBinding

class CommunityPage : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCommunityPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up navigation bar clicks
        binding.navHome.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
        }

        binding.navProfile.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        // Group Buttons Click Listeners
        binding.btnMindfulTogether.setOnClickListener {
            navigateToChatGroup("Mindful Together")
        }

        binding.btnSilentStruggles.setOnClickListener {
            navigateToChatGroup("Silent Struggles")
        }

        // Handle navigation bar click events
        binding.navHome.setOnClickListener {
            startActivity(Intent(this, TherapyPage::class.java))
        }

        binding.navTherapy.setOnClickListener {
            startActivity(Intent(this, TherapyPage::class.java))
        }

        binding.navMood.setOnClickListener {
            startActivity(Intent(this, MoodPage::class.java))
        }

        binding.navProfile.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }
    }

    private fun navigateToChatGroup(groupName: String) {
        val intent = Intent(this, CommunityChatPage::class.java)
        intent.putExtra("groupName", groupName)
        startActivity(intent)
    }
}
