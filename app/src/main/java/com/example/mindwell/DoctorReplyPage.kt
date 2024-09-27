package com.example.mindwell

import ChatAdapter
import ChatMessage
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindwell.databinding.ActivityDoctorReplyPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DoctorReplyPage : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorReplyPageBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var doctorId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDoctorReplyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Get userId from intent and doctorId from authentication
        userId = intent.getStringExtra("userId") ?: ""
        userName = intent.getStringExtra("username") ?: ""
        doctorId = auth.currentUser?.uid ?: ""

        // Set the chat header with user ID (could be username if stored)
        binding.tvChatWithUser.text = "Chat with $userName"

        // Set up RecyclerView
        chatAdapter = ChatAdapter(doctorId) // Pass the doctorId for message alignment
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewChat.adapter = chatAdapter

        // Load messages from Firestore
        loadMessages()

        // Handle Send Button click
        binding.btnSend.setOnClickListener {
            val messageText = binding.etMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                binding.etMessage.text.clear()
            } else {
                Toast.makeText(this, "Enter a message", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Back Button click
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    // Load messages from Firestore in real-time
    private fun loadMessages() {
        val chatId = "$userId-$doctorId"
        db.collection("chats")
            .document(chatId)
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
                }
            }
    }

    // Send message to Firestore
    private fun sendMessage(messageText: String) {
        val message = hashMapOf(
            "text" to messageText,
            "sender" to doctorId,
            "timestamp" to System.currentTimeMillis()
        )

        val chatId = "$userId-$doctorId"

        db.collection("chats").document(chatId).collection("messages")
            .add(message)
            .addOnSuccessListener {
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to send message: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
