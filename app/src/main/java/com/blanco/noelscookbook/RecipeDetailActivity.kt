package com.blanco.noelscookbook

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.blanco.noelscookbook.databinding.ActivityRecipeDetailBinding
import com.blanco.noelscookbook.recyclerviewadapters.CookingStepAdapterDisplayOnly
import com.blanco.noelscookbook.recyclerviewadapters.IngredientAdapterDisplayOnly
import com.blanco.noelscookbook.repositories.CookingStepRepository
import com.blanco.noelscookbook.repositories.IngredientRepository
import com.blanco.noelscookbook.repositories.RecipeRepository
import com.blanco.noelscookbook.room.AppDatabase
import com.blanco.noelscookbook.room.CookingStep
import com.blanco.noelscookbook.room.Ingredient
import com.blanco.noelscookbook.room.Recipe
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDetailBinding
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var ingredientRepository: IngredientRepository
    private lateinit var cookingStepRepository: CookingStepRepository
    private lateinit var ingredientAdapter: IngredientAdapterDisplayOnly
    private lateinit var cookingStepAdapter: CookingStepAdapterDisplayOnly
    private val ingredientList = mutableListOf<Ingredient>()
    private val cookingStepList = mutableListOf<CookingStep>()
    private var recipeId: Long = 0L

    @SuppressLint("NotifyDataSetChanged")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecipeRepository
        val recipeDao = AppDatabase.getDatabase(application).recipeDao()
        recipeRepository = RecipeRepository(recipeDao)

        // Initialize IngredientRepository
        val ingredientDao = AppDatabase.getDatabase(application).ingredientDao()
        ingredientRepository = IngredientRepository(ingredientDao)

        // Initialize CookingStepRepository
        val cookingStepDao = AppDatabase.getDatabase(application).cookingStepDao()
        cookingStepRepository = CookingStepRepository(cookingStepDao)

        // Retrieve recipe details from intent
        recipeId = intent.getLongExtra("recipeId", 0)

        GlobalScope.launch(Dispatchers.Main) {
            val recipe = recipeRepository.getRecipeById(recipeId)

            // Retrieve ingredients and cooking steps based on recipeId
            val ingredients = ingredientRepository.getIngredientsByRecipeId(recipeId)
            val cookingSteps = cookingStepRepository.getCookingStepsByRecipeId(recipeId)

            // Update UI with recipe details
            updateUIWithRecipeDetails(recipe)

            // Update UI with ingredients
            ingredientList.addAll(ingredients)
            ingredientAdapter.notifyDataSetChanged()

            // Update UI with cooking steps
            cookingStepList.addAll(cookingSteps)
            cookingStepAdapter.notifyDataSetChanged()

            // Log the cooking step list after it's fully loaded
            Log.d("b", "at cooking list: $cookingStepList")
        }


        // Initialize your RecyclerView and Adapter for Ingredients
        ingredientAdapter = IngredientAdapterDisplayOnly(ingredientList)
        binding.recyclerViewIngredients.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewIngredients.adapter = ingredientAdapter

        // Initialize your RecyclerView and Adapter for Cooking Steps
        cookingStepAdapter = CookingStepAdapterDisplayOnly(cookingStepList)
        binding.recyclerViewCookingProcess.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCookingProcess.adapter = cookingStepAdapter


    }

    private fun updateUIWithRecipeDetails(recipe: Recipe) {
        // Update your UI elements with recipe details
        binding.textviewRecipeName.text = recipe.name
        binding.textviewNumberOfServings.text = getString(R.string.servings_text, recipe.servings)
        binding.textviewCookingTime.text = getString(R.string.cooking_time_text, recipe.cookingTime)
        binding.textviewCuisineType.text = getString(R.string.cuisine_type_text, recipe.cuisineType)

    }
}