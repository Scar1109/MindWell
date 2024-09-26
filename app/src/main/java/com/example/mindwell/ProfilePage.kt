package com.example.mindwell

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mindwell.databinding.ActivityProfilePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ProfilePage : AppCompatActivity() {

    private lateinit var binding: ActivityProfilePageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Check if the user is logged in
        checkUserStatus()

        // Load user data from Firestore
        loadUserData()

        // Handle navigation bar click events
        binding.navHome.setOnClickListener {
            // Already on the home page, show a message
            startActivity(Intent(this, HomePage::class.java))
        }

        binding.navTherapy.setOnClickListener {
            startActivity(Intent(this, TherapyPage::class.java))
        }

        binding.navCommunity.setOnClickListener {
            startActivity(Intent(this, CommunityPage::class.java))
        }

        binding.navMood.setOnClickListener {
            startActivity(Intent(this, MoodPage::class.java))
        }

        // Handle Logout Button
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginSignupPage::class.java))
            finish()
        }
    }

    // Check if user is logged in, if not redirect to LoginSignupPage
    private fun checkUserStatus() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // User is not logged in, redirect to Login/Signup page
            Toast.makeText(this, "Please log in to access your profile", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginSignupPage::class.java))
            finish() // Close the ProfilePage to prevent access without logging in
        }
    }

    // Load user data from Firestore
    private fun loadUserData() {
        val user: FirebaseUser? = auth.currentUser
        val uid = user?.uid

        if (uid != null) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val username = document.getString("username") ?: "No Name"
                        val email = document.getString("email") ?: "No Email"
                        val phoneNumber = document.getString("phoneNumber") ?: "No Phone Number"
                        val gender = document.getString("gender") ?: "Not Provided"

                        // Set user data on TextViews
                        binding.tvProfileName.text = username
                        binding.tvProfileDetails.text = "Email - $email\nPhone Number - $phoneNumber\nGender - $gender"

                        // Set button text based on whether phone number and gender are provided
                        if (phoneNumber == "No Phone Number" || gender == "Not Provided") {
                            binding.btnEditPhoneGender.text = "Add Phone & Gender"
                        } else {
                            binding.btnEditPhoneGender.text = "Edit Phone & Gender"
                        }

                        // Set click listener for adding/editing phone number and gender
                        binding.btnEditPhoneGender.setOnClickListener {
                            showEditProfileDialog(phoneNumber, gender)
                        }
                    } else {
                        Toast.makeText(this, "No data found for this user", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load profile data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Function to show the dialog for editing phone number and gender
    private fun showEditProfileDialog(phoneNumber: String, gender: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_edit_profile)

        val etPhoneNumber = dialog.findViewById<EditText>(R.id.et_phone_number)
        val radioGroupGender = dialog.findViewById<RadioGroup>(R.id.radioGroup_gender)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save_profile)

        // Pre-fill the dialog with existing phone number and gender
        if (phoneNumber != "No Phone Number") {
            etPhoneNumber.setText(phoneNumber)
        }

        if (gender == "Male") {
            radioGroupGender.check(R.id.rb_male)
        } else if (gender == "Female") {
            radioGroupGender.check(R.id.rb_female)
        }

        // Handle Save button click
        btnSave.setOnClickListener {
            val updatedPhoneNumber = etPhoneNumber.text.toString().trim()
            val updatedGender = when (radioGroupGender.checkedRadioButtonId) {
                R.id.rb_male -> "Male"
                R.id.rb_female -> "Female"
                else -> "Not Provided"
            }

            if (updatedPhoneNumber.isNotEmpty()) {
                saveUserData(updatedPhoneNumber, updatedGender)

                // Update UI immediately after saving, without reloading from Firestore
                binding.tvProfileDetails.text = "Email - ${auth.currentUser?.email}\nPhone Number - $updatedPhoneNumber\nGender - $updatedGender"
                binding.btnEditPhoneGender.text = "Edit Phone & Gender"

                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show()
            }
        }


        dialog.show()
    }

    // Save user data to Firestore and update UI without reloading data
    private fun saveUserData(phoneNumber: String, gender: String) {
        val user: FirebaseUser? = auth.currentUser
        val uid = user?.uid

        if (uid != null) {
            // Update phone number and gender in Firestore
            val userData = hashMapOf(
                "phoneNumber" to phoneNumber,
                "gender" to gender
            )

            db.collection("users").document(uid).set(userData, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()

                    // Directly update the UI without reloading data
                    binding.tvProfileDetails.text = "Email - ${user.email}\nPhone Number - $phoneNumber\nGender - $gender"

                    // Update the button text to reflect that the user can now edit the details
                    binding.btnEditPhoneGender.text = "Edit Phone & Gender"
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
        }
    }

}
