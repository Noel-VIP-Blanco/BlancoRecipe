package com.blanco.noelscookbook

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.blanco.noelscookbook.bottomsheetfragments.AddCookingStepBottomSheetDialogFragment
import com.blanco.noelscookbook.bottomsheetfragments.AddIngredientBottomSheetDialogFragment
import com.blanco.noelscookbook.databinding.ActivityUpdateRecipeBinding
import com.blanco.noelscookbook.recyclerviewadapters.CookingStepAdapter
import com.blanco.noelscookbook.recyclerviewadapters.IngredientAdapter
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

class UpdateRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateRecipeBinding
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var ingredientRepository: IngredientRepository
    private lateinit var cookingStepRepository: CookingStepRepository
    private lateinit var ingredientAdapter: IngredientAdapter
    private lateinit var cookingStepAdapter: CookingStepAdapter
    private val ingredientList = mutableListOf<Ingredient>()
    private val cookingStepList = mutableListOf<CookingStep>()
    private var recipeId: Long = 0L


    @SuppressLint("NotifyDataSetChanged")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateRecipeBinding.inflate(layoutInflater)
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

           //Re set button text
            binding.btnSubmitRecipe.text = getString(R.string.update_button_text)
        }


        // Initialize your RecyclerView and Adapter for Ingredients
        ingredientAdapter = IngredientAdapter(ingredientList)
        binding.recyclerViewIngredients.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewIngredients.adapter = ingredientAdapter

        // Initialize your RecyclerView and Adapter for Cooking Steps
        cookingStepAdapter = CookingStepAdapter(cookingStepList)
        binding.recyclerViewCookingProcess.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCookingProcess.adapter = cookingStepAdapter


        // Set click listener for the "Add Ingredient" button
        // Set click listener for the "Add Ingredient" button
        binding.btnAddIngredient.setOnClickListener {
            // Show the bottom sheet dialog
            val bottomSheetDialogFragment =
                AddIngredientBottomSheetDialogFragment(
                    recipeId,
                    ingredient = null,
                    onUpdate = { ingredient ->
                        // Add the new ingredient to the list
                        ingredientList.add(ingredient)
                        // Notify the adapter that the data set has changed
                        ingredientAdapter.notifyDataSetChanged()
                    }
                )
            bottomSheetDialogFragment.show(
                supportFragmentManager,
                bottomSheetDialogFragment.tag
            )
        }


        // Set click listener for the "Add Cooking Process" button
        binding.btnAddCookingProcess.setOnClickListener {
            // Show the bottom sheet dialog
            val bottomSheetDialogFragment = AddCookingStepBottomSheetDialogFragment(
                recipeId,
                cookingStep = null, // Pass the initial cooking step if editing, or null if adding a new one
                onUpdate = { newCookingStep ->
                    // Add the new or updated cooking step to the list
                    cookingStepList.add(newCookingStep)
                    // Notify the adapter that the data set has changed
                    cookingStepAdapter.notifyDataSetChanged()
                }
            )

            // Show the bottom sheet dialog
            bottomSheetDialogFragment.show(
                supportFragmentManager,
                bottomSheetDialogFragment.tag
            )
        }


        // Set click listener for the "Submit Recipe" button
        binding.btnSubmitRecipe.setOnClickListener {
            handleSubmitRecipe()
        }





    }


    private fun updateUIWithRecipeDetails(recipe: Recipe) {
        // Update your UI elements with recipe details
        binding.editRecipeName.setText(recipe.name)
        binding.editServings.setText(recipe.servings.toString())
        binding.editCookingTime.setText(recipe.cookingTime.toString())
        binding.editCuisineType.setText(recipe.cuisineType)
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun handleSubmitRecipe() {
        // Access views using the binding object
        val recipeName = binding.editRecipeName.text.toString()
        val servings = binding.editServings.text.toString()
        val cookingTime = binding.editCookingTime.text.toString()
        val cuisineType = binding.editCuisineType.text.toString()

        // Check for null or empty values
        if (recipeName.isBlank() || servings.isBlank() || cookingTime.isBlank() || cuisineType.isBlank()) {
            // Display an alert dialog for empty fields
            showAlert()
        } else {
            // Check if you're updating an existing recipe or creating a new one
            if (isUpdatingExistingRecipe()) {
                // Retrieve the existing recipe details
                val existingRecipeId = intent.getLongExtra("recipeId", 0)
                GlobalScope.launch(Dispatchers.Main) {
                    val existingRecipe = recipeRepository.getRecipeById(existingRecipeId)

                    // Update the existing recipe details
                    existingRecipe.name = recipeName
                    existingRecipe.servings = servings.toInt()
                    existingRecipe.cookingTime = cookingTime.toInt()
                    existingRecipe.cuisineType = cuisineType

                    // Update the recipe in the database
                    recipeRepository.update(existingRecipe)

                    // Update, insert, or delete ingredients
                    updateInsertDeleteIngredients(existingRecipeId)

                    // Update, insert, or delete cooking steps
                    updateInsertDeleteCookingSteps(existingRecipeId)

                    finish()
                }
            } else {
                // Create a new Recipe object
                val newRecipe = Recipe(
                    name = recipeName,
                    servings = servings.toInt(),
                    cookingTime = cookingTime.toInt(),
                    cuisineType = cuisineType
                )

                // Insert the new recipe into the database
                GlobalScope.launch(Dispatchers.Main) {
                    val newRecipeId = recipeRepository.insert(newRecipe)

                    // Log the new recipeId to check if it was inserted successfully
                    Log.d("Recipe Insertion", "Recipe ID: $newRecipeId")

                    // Update or insert ingredients
                    updateOrInsertIngredients(newRecipeId)

                    // Update or insert cooking steps
                    updateOrInsertCookingSteps(newRecipeId)

                    // Clear the temporary lists after successful submission.
                    ingredientList.clear()
                    cookingStepList.clear()

                    finish()
                }
            }
        }
    }

    private suspend fun updateInsertDeleteIngredients(recipeId: Long) {
        val originalIngredients = ingredientRepository.getIngredientsByRecipeId(recipeId)

        for (originalIngredient in originalIngredients) {
            // If an original ingredient is not found in the updated list, delete it
            if (ingredientList.none { it.id == originalIngredient.id }) {
                ingredientRepository.delete(originalIngredient)
            }
        }

        for (ingredient in ingredientList) {
            ingredient.recipeId = recipeId
            when {
                ingredient.id != 0L -> {
                    // Update existing ingredient
                    ingredientRepository.update(ingredient)
                }
                else -> {
                    // Insert new ingredient
                    ingredientRepository.insert(ingredient)
                }
            }
        }
    }

    private suspend fun updateInsertDeleteCookingSteps(recipeId: Long) {
        val originalCookingSteps = cookingStepRepository.getCookingStepsByRecipeId(recipeId)

        for (originalCookingStep in originalCookingSteps) {
            // If an original cooking step is not found in the updated list, delete it
            if (cookingStepList.none { it.id == originalCookingStep.id }) {
                cookingStepRepository.delete(originalCookingStep)
            }
        }

        for (cookingStep in cookingStepList) {
            cookingStep.recipeId = recipeId
            when {
                cookingStep.id != 0L -> {
                    // Update existing cooking step
                    cookingStepRepository.update(cookingStep)
                }
                else -> {
                    // Insert new cooking step
                    cookingStepRepository.insert(cookingStep)
                }
            }
        }
    }

    private suspend fun updateOrInsertIngredients(recipeId: Long) {
        for (ingredient in ingredientList) {
            ingredient.recipeId = recipeId
            if (ingredient.id != 0L) {
                // Update existing ingredient
                ingredientRepository.update(ingredient)
            } else {
                // Insert new ingredient
                ingredientRepository.insert(ingredient)
            }
        }
    }

    private suspend fun updateOrInsertCookingSteps(recipeId: Long) {
        for (cookingStep in cookingStepList) {
            cookingStep.recipeId = recipeId
            if (cookingStep.id != 0L) {
                // Update existing cooking step
                cookingStepRepository.update(cookingStep)
            } else {
                // Insert new cooking step
                cookingStepRepository.insert(cookingStep)
            }
        }
    }

    private fun isUpdatingExistingRecipe(): Boolean {
        // Check if you have an existing recipeId from the intent
        val existingRecipeId = intent.getLongExtra("recipeId", 0)
        return existingRecipeId != 0L
    }


    private fun showAlert() {
        AlertDialog.Builder(this).setTitle("Incomplete Entries").setMessage("Please fill in empty fields")
            .setPositiveButton("OK", null).show()
    }

}


