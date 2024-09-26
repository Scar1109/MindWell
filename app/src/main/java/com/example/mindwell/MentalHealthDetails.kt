package com.example.mindwell

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mindwell.databinding.ActivityMentalHealthDetailsBinding

class MentalHealthDetails : AppCompatActivity() {

    private lateinit var binding: ActivityMentalHealthDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMentalHealthDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button functionality
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish() // Finish activity and return to previous one
        }
    }
}
