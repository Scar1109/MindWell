package com.example.mindwell

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindwell.databinding.ActivityDoctorUserListPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DoctorUserListPage : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorUserListPageBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userListAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDoctorUserListPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Set up RecyclerView
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(this)
        userListAdapter = UserAdapter { user ->
            navigateToChat(user)
        }
        binding.recyclerViewUsers.adapter = userListAdapter

        // Handle Logout Button
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginSignupPage::class.java))
            finish()
        }

        // Load user list
        loadUserList()

    }

    // Load user list from Firestore
    private fun loadUserList() {
        db.collection("users")
            .whereEqualTo("userType", "User")
            .get()
            .addOnSuccessListener { documents ->
                val users = documents.map { document ->
                    User(
                        userId = document.id, // Assuming the document ID is the user's UID
                        username = document.getString("username") ?: "No Name"
                    )
                }
                userListAdapter.submitList(users)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load user list", Toast.LENGTH_SHORT).show()
            }
    }

    // Navigate to DoctorChatReplyPage when a user is clicked
    private fun navigateToChat(user: User) {
        val intent = Intent(this, DoctorReplyPage::class.java)
        intent.putExtra("userId", user.userId)
        intent.putExtra("username", user.username)
        startActivity(intent)
    }
}
