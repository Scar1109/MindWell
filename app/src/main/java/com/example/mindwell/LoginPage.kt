package com.example.mindwell

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mindwell.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.backBtn.setOnClickListener {
            finish()
        }

        // Handle Login button click
        binding.loginBtn.setOnClickListener {
            val email = binding.lgnEmail.text.toString().trim()
            val password = binding.pwdInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                    // Check the user type from Firestore
                    FirebaseFirestore.getInstance().collection("users")
                        .document(userId).get()
                        .addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                val userType = document.getString("userType") ?: "User"
                                if (userType == "Doctor") {
                                    // Redirect to DoctorReplyPage if the user is a doctor
                                    startActivity(Intent(this, DoctorUserListPage::class.java))
                                } else {
                                    // Redirect to HomePage if the user is not a doctor
                                    startActivity(Intent(this, HomePage::class.java))
                                }
                                finish() // Close the LoginPage
                            }
                        }
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
