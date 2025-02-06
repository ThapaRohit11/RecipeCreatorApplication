package com.example.recipefinderapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinderapplication.databinding.ListItemRecipeBinding
import com.example.recipefinderapplication.ui.model.Recipe

class RecipeAdapter(private val recipeList: List<Recipe>, private val onItemClick: (Recipe) -> Unit) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ListItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.bind(recipe, onItemClick)
    }

    override fun getItemCount(): Int = recipeList.size

    class RecipeViewHolder(private val binding: ListItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe, onItemClick: (Recipe) -> Unit) {
            binding.recipeTitle.text = recipe.title
            itemView.setOnClickListener { onItemClick(recipe) }
        }
    }
}
