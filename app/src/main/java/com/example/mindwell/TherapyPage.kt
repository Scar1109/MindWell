package com.example.mindwell

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindwell.databinding.ActivityTherapyPageBinding
import com.google.firebase.firestore.FirebaseFirestore

class TherapyPage : AppCompatActivity() {

    private lateinit var binding: ActivityTherapyPageBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var doctorListAdapter: DoctorListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTherapyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle navigation bar click events
        binding.navHome.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
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

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        // Set up RecyclerView
        binding.recyclerViewDoctors.layoutManager = LinearLayoutManager(this)
        doctorListAdapter = DoctorListAdapter { doctorName, doctorUID ->
            navigateToChat(doctorName, doctorUID)
        }
        binding.recyclerViewDoctors.adapter = doctorListAdapter

        // Load doctors from Firestore
        loadDoctors()
    }

    // Load doctors from Firestore where userType == "Doctor"
    private fun loadDoctors() {
        db.collection("users")
            .whereEqualTo("userType", "Doctor")
            .get()
            .addOnSuccessListener { documents ->
                val doctors = documents.map { document ->
                    mapOf(
                        "name" to (document.getString("username") ?: "No Name"),
                        "specialty" to (document.getString("specialty") ?: "No Specialty"),
                        "uid" to document.id // Getting doctor UID from document ID
                    )
                }
                doctorListAdapter.submitList(doctors)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load doctors", Toast.LENGTH_SHORT).show()
            }
    }

    // Navigate to the ChatPage when a doctor is clicked
    private fun navigateToChat(doctorName: String, doctorUID: String) {
        val intent = Intent(this, DoctorChatPage::class.java)
        intent.putExtra("doctorName", doctorName)
        intent.putExtra("doctorUID", doctorUID)
        startActivity(intent)
    }
}
