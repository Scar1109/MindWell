package com.example.mindwell

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.mindwell.databinding.ActivityMentalHealthTipsBinding

class MentalHealthTips : AppCompatActivity() {

    private lateinit var binding: ActivityMentalHealthTipsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMentalHealthTipsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button functionality
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish() // Finish activity and return to previous one
        }
    }
}
