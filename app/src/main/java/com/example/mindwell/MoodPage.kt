package com.example.mindwell

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mindwell.databinding.ActivityMoodPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MoodPage : AppCompatActivity() {

    private lateinit var binding: ActivityMoodPageBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMoodPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Load previously saved mood data
        loadPreviousMoodData()

        // Handle navigation bar click events
        binding.navHome.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
        }

        binding.navTherapy.setOnClickListener {
            startActivity(Intent(this, TherapyPage::class.java))
        }

        binding.navCommunity.setOnClickListener {
            startActivity(Intent(this, CommunityPage::class.java))
        }

        binding.navProfile.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        // Handle Submit Button click
        binding.btnSubmit.setOnClickListener {
            val anxietyRating = binding.etAnxietyRating.text.toString().trim()
            val sleepRating = binding.etSleepRating.text.toString().trim()
            val energyRating = binding.etEnergyRating.text.toString().trim()
            val stressRating = binding.etStressRating.text.toString().trim()
            val supportRating = binding.etSupportRating.text.toString().trim()

            if (validateInput(anxietyRating, sleepRating, energyRating, stressRating, supportRating)) {
                saveMoodData(anxietyRating, sleepRating, energyRating, stressRating, supportRating)
            } else {
                Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Validate input fields
    private fun validateInput(vararg fields: String): Boolean {
        return fields.all { it.isNotEmpty() }
    }

    // Save mood data to Firestore
    private fun saveMoodData(
        anxietyRating: String,
        sleepRating: String,
        energyRating: String,
        stressRating: String,
        supportRating: String
    ) {
        val userId = auth.currentUser?.uid ?: return
        val moodData = hashMapOf(
            "anxietyRating" to anxietyRating,
            "sleepRating" to sleepRating,
            "energyRating" to energyRating,
            "stressRating" to stressRating,
            "supportRating" to supportRating,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("moodEntries").document(userId)
            .collection("entries")
            .add(moodData)
            .addOnSuccessListener {
                Toast.makeText(this, "Mood data saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save mood data: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Load previously saved mood data from Firestore
    private fun loadPreviousMoodData() {
        val userId = auth.currentUser?.uid ?: return

        // Fetch the most recent mood entry
        db.collection("moodEntries").document(userId)
            .collection("entries")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val moodEntry = documents.first()
                    binding.etAnxietyRating.setText(moodEntry.getString("anxietyRating"))
                    binding.etSleepRating.setText(moodEntry.getString("sleepRating"))
                    binding.etEnergyRating.setText(moodEntry.getString("energyRating"))
                    binding.etStressRating.setText(moodEntry.getString("stressRating"))
                    binding.etSupportRating.setText(moodEntry.getString("supportRating"))
                } else {
                    Toast.makeText(this, "No previous mood data found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load mood data: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
