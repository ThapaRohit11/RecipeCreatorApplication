package com.example.recipefinderapplication.ui.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinderapplication.databinding.ListItemRecipeBinding
import com.example.recipefinderapplication.ui.model.Recipe
import com.google.firebase.database.FirebaseDatabase

class RecipeAdapter(
    private val recipeList: MutableList<Recipe>,
    private val onRecipeUpdated: () -> Unit, // Callback for updating
    private val onRecipeClick: (Recipe) -> Unit // Callback for clicking a recipe
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ListItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding, onRecipeClick)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.bind(recipe, onRecipeUpdated)
    }

    override fun getItemCount(): Int = recipeList.size

    class RecipeViewHolder(
        private val binding: ListItemRecipeBinding,
        private val onRecipeClick: (Recipe) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe, onRecipeUpdated: () -> Unit) {
            binding.recipeTitle.text = recipe.title

            // Click the whole card to open RecipeDetailsFragment
            binding.root.setOnClickListener {
                onRecipeClick(recipe)
            }

            // Update Button Click
            binding.btnUpdate.setOnClickListener {
                showUpdateDialog(recipe, onRecipeUpdated)
            }

            // Delete Button Click
            binding.btnDelete.setOnClickListener {
                val databaseRef = FirebaseDatabase.getInstance().getReference("recipes").child(recipe.id)
                databaseRef.removeValue().addOnSuccessListener {
                    Toast.makeText(binding.root.context, "Recipe Deleted", Toast.LENGTH_SHORT).show()
                    onRecipeUpdated()
                }
            }
        }

        private fun showUpdateDialog(recipe: Recipe, onRecipeUpdated: () -> Unit) {
            val context = binding.root.context
            val dialogView = LayoutInflater.from(context).inflate(com.example.recipefinderapplication.R.layout.dialog_update_recipe, null)
            val editTitle = dialogView.findViewById<EditText>(com.example.recipefinderapplication.R.id.editRecipeTitle)
            val editSteps = dialogView.findViewById<EditText>(com.example.recipefinderapplication.R.id.editRecipeSteps)

            editTitle.setText(recipe.title)
            editSteps.setText(recipe.steps)

            AlertDialog.Builder(context)
                .setTitle("Update Recipe")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    val updatedTitle = editTitle.text.toString().trim()
                    val updatedSteps = editSteps.text.toString().trim()

                    if (updatedTitle.isNotEmpty() && updatedSteps.isNotEmpty()) {
                        updateRecipeInFirebase(recipe.id, updatedTitle, updatedSteps, onRecipeUpdated)
                    } else {
                        Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        private fun updateRecipeInFirebase(recipeId: String, newTitle: String, newSteps: String, onRecipeUpdated: () -> Unit) {
            val databaseRef = FirebaseDatabase.getInstance().getReference("recipes").child(recipeId)

            val updates = mapOf(
                "title" to newTitle,
                "steps" to newSteps
            )

            databaseRef.updateChildren(updates).addOnSuccessListener {
                Toast.makeText(binding.root.context, "Recipe Updated", Toast.LENGTH_SHORT).show()
                onRecipeUpdated()
            }.addOnFailureListener {
                Toast.makeText(binding.root.context, "Failed to update recipe", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
