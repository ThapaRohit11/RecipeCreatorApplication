package com.example.recipefinderapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.recipefinderapplication.databinding.FragmentAddBinding
import com.example.recipefinderapplication.ui.model.Recipe
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference("recipes")

        // Handle the Add Recipe button click
        binding.addRecipeButton.setOnClickListener {
            val title = binding.recipeTitleInput.text.toString()
            val steps = binding.recipeStepsInput.text.toString()

            if (title.isEmpty() || steps.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in both fields", Toast.LENGTH_SHORT).show()
            } else {
                // Create a new Recipe object and add it to the Firebase database
                val recipeId = database.push().key ?: return@setOnClickListener
                val recipe = Recipe(recipeId, title, steps)

                // Save the recipe to Firebase
                database.child(recipeId).setValue(recipe)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Recipe added successfully", Toast.LENGTH_SHORT).show()
                        // Optionally clear the input fields
                        binding.recipeTitleInput.text.clear()
                        binding.recipeStepsInput.text.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to add recipe", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        return binding.root
    }
}
