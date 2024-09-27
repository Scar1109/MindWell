package com.example.mindwell

import ChatAdapter
import ChatMessage
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindwell.databinding.ActivityCommunityChatPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CommunityChatPage : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityChatPageBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var chatGroupName: String
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCommunityChatPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the chat group name passed from the previous activity
        chatGroupName = intent.getStringExtra("groupName") ?: "Default Group"

        // Set the group name in the TextView
        binding.tvGroupName.text = chatGroupName

        // Initialize Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid ?: ""

        // Handle back button click
        binding.backBtn.setOnClickListener {
            finish() // Go back to the previous screen
        }

        // Set up RecyclerView for chat messages
        chatAdapter = ChatAdapter(userId) // Pass the current user ID to the adapter
        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMessages.adapter = chatAdapter

        // Load chat messages from Firestore
        loadMessages()

        // Handle Send Button click
        binding.btnSend.setOnClickListener {
            val messageText = binding.etMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                binding.etMessage.text.clear()
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Load chat messages from Firestore in real-time
    private fun loadMessages() {
        db.collection("communityChats")
            .document(chatGroupName) // Use the chat group name as the document ID
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, "Failed to load messages: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    val messages = snapshots.map { document ->
                        ChatMessage(
                            text = document.getString("text") ?: "",
                            sender = document.getString("sender") ?: "",
                            timestamp = document.getLong("timestamp") ?: 0L
                        )
                    }

                    chatAdapter.submitList(messages)

                    // Ensure RecyclerView is populated and position is valid
                    val itemCount = chatAdapter.itemCount
                    if (itemCount > 0) {
                        binding.recyclerViewMessages.smoothScrollToPosition(itemCount - 1) // Scroll to the last message
                    }
                }
            }
    }

    // Send message to Firestore
    private fun sendMessage(messageText: String) {
        val message = hashMapOf(
            "text" to messageText,
            "sender" to userId,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("communityChats").document(chatGroupName)
            .collection("messages").add(message)
            .addOnSuccessListener {
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to send message: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
