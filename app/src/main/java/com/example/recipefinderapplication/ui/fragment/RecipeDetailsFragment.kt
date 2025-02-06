package com.example.recipefinderapplication.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipefinderapplication.databinding.FragmentRecipeDetailsBinding

class RecipeDetailsFragment : Fragment() {
    private lateinit var binding: FragmentRecipeDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)

        // Get the recipe title and steps from the arguments
        val title = arguments?.getString("recipeTitle")
        val steps = arguments?.getString("recipeSteps")

        binding.recipeTitle.text = title
        binding.recipeSteps.text = steps

        return binding.root
    }
}
