package com.example.recipefinderapplication.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.recipefinderapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.example.recipefinderapplication.ui.activity.LoginActivity

class ProfileFragment : Fragment() {

    private lateinit var userEmailTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize UI elements
        userEmailTextView = view.findViewById(R.id.userEmailTextView)
        logoutButton = view.findViewById(R.id.logoutButton)

        // Get the logged-in user's email from Firebase
        val currentUser = auth.currentUser
        if (currentUser != null) {
            userEmailTextView.text = currentUser.email
        } else {
            userEmailTextView.text = "No user logged in"
        }

        // Set up logout button
        logoutButton.setOnClickListener {
            auth.signOut()
            navigateToLoginActivity()
        }

        return view
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()  // Close current activity to avoid back navigation to profile
    }
}
