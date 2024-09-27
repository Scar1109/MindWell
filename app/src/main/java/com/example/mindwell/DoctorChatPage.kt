package com.example.mindwell

import ChatAdapter
import ChatMessage
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindwell.databinding.ActivityDoctorChatPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DoctorChatPage : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorChatPageBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var doctorName: String
    private lateinit var doctorUID: String // Store doctor UID
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDoctorChatPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Get doctor name and UID passed from previous activity
        doctorName = intent.getStringExtra("doctorName") ?: "Doctor"
        doctorUID = intent.getStringExtra("doctorUID") ?: "" // Get the doctor's UID
        userId = auth.currentUser?.uid ?: ""

        binding.tvChatWithDoctor.text = "Dr. $doctorName"

        // Set up RecyclerView for chat messages
        chatAdapter = ChatAdapter(userId) // Pass the userId to adapter
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewChat.adapter = chatAdapter

        // Load chat messages from Firestore
        loadMessages()

        binding.backBtn.setOnClickListener {
            finish()
        }

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
    }

    // Load chat messages from Firestore in real-time
    private fun loadMessages() {
        db.collection("chats")
            .document("$userId-$doctorUID") // Use doctorUID for unique chat identifier
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
        val message: HashMap<String, Any> = hashMapOf(
            "text" to messageText,
            "sender" to userId,
            "timestamp" to System.currentTimeMillis()
        )

        // Define a document for the chat (User-Doctor unique identifier)
        val chatId = "$userId-$doctorUID" // Use doctorUID here

        // Add a new chat document if it doesn't exist
        db.collection("chats").document(chatId).get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    // Create a new chat if it doesn't exist
                    val participants: HashMap<String, Any> = hashMapOf(
                        "participants" to listOf(userId, doctorUID) // Save correct doctorUID here
                    )

                    db.collection("chats").document(chatId).set(participants)
                        .addOnSuccessListener {
                            // Once chat document is created, send the message
                            addMessageToChat(chatId, message)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to create chat: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // If chat exists, send the message
                    addMessageToChat(chatId, message)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to check chat existence: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addMessageToChat(chatId: String, message: Map<String, Any>) {
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
