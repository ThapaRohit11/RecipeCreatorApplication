package com.example.recipefinderapplication.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinderapplication.R
import com.example.recipefinderapplication.databinding.FragmentHomeBinding
import com.example.recipefinderapplication.ui.adapter.RecipeAdapter
import com.example.recipefinderapplication.ui.model.Recipe
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var database: DatabaseReference
    private lateinit var recipeList: MutableList<Recipe>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        recyclerView = binding.recipesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        recipeList = mutableListOf()
        recipeAdapter = RecipeAdapter(recipeList,
            onRecipeUpdated = { loadRecipes() },
            onRecipeClick = { recipe -> openRecipeDetails(recipe) }
        )
        recyclerView.adapter = recipeAdapter

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance().getReference("recipes")

        // Fetch recipes from Firebase
        loadRecipes()

        return binding.root
    }

    private fun loadRecipes() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recipeList.clear()
                if (!snapshot.hasChildren()) {
                    Toast.makeText(context, "No recipes available", Toast.LENGTH_SHORT).show()
                }
                for (dataSnapshot in snapshot.children) {
                    val recipe = dataSnapshot.getValue(Recipe::class.java)
                    recipe?.let { recipeList.add(it) }
                }
                recipeAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load recipes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openRecipeDetails(recipe: Recipe) {
        val bundle = Bundle().apply {
            putString("recipeTitle", recipe.title)
            putString("recipeSteps", recipe.steps)
        }
        val recipeDetailsFragment = RecipeDetailsFragment().apply {
            arguments = bundle
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, recipeDetailsFragment)
            .addToBackStack(null)
            .commit()
    }
}
