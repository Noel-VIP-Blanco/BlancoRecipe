package com.blanco.noelscookbook

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blanco.noelscookbook.bottomsheetfragments.AddCookingStepBottomSheetDialogFragment
import com.blanco.noelscookbook.bottomsheetfragments.AddIngredientBottomSheetDialogFragment
import com.blanco.noelscookbook.databinding.ActivityAddArecipeBinding
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

class AddARecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddArecipeBinding
    private val ingredientList = mutableListOf<Ingredient>()
    private lateinit var ingredientAdapter: IngredientAdapter
    private val cookingStepList = mutableListOf<CookingStep>()
    private lateinit var cookingStepAdapter: CookingStepAdapter
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var ingredientRepository: IngredientRepository
    private lateinit var cookingStepRepository: CookingStepRepository
    private var recipeId: Long = 0L


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddArecipeBinding.inflate(layoutInflater)
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


        // Initialize your RecyclerView and Adapter for Ingredients
        ingredientAdapter = IngredientAdapter(ingredientList)
        binding.recyclerViewIngredients.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewIngredients.adapter = ingredientAdapter

        // Initialize your RecyclerView and Adapter for Cooking Steps
        cookingStepAdapter = CookingStepAdapter(cookingStepList)
        binding.recyclerViewCookingProcess.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCookingProcess.adapter = cookingStepAdapter

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
            // Create a Recipe object or perform any desired action
            val recipe = Recipe(
                name = recipeName,
                servings = servings.toInt(),
                cookingTime = cookingTime.toInt(),
                cuisineType = cuisineType
            )

            // Insert the recipe into the database or perform any desired action
            GlobalScope.launch(Dispatchers.Main) {
                // Insert the new recipe into the database
                recipeId = recipeRepository.insert(recipe)

                // Update the recipeId in the ingredientList
                ingredientList.forEach { it.recipeId = recipeId }

                // Insert ingredients
                for (ingredient in ingredientList) {
                        ingredientRepository.insert(ingredient)
                }

                // Log all ingredients after insertion
                val allIngredients = ingredientRepository.getAllIngredients()
                Log.d("All Ingredients", "Ingredients: $allIngredients")

                // Update the recipeId in the ingredientList
                cookingStepList.forEach { it.recipeId = recipeId }

                // Insert cooking steps
                for (cookingStep in cookingStepList) {
                        cookingStepRepository.insert(cookingStep)
                }




                // Clear the temporary lists after successful submission.
                ingredientList.clear()
                cookingStepList.clear()

                finish()


            }

        }
    }

    private fun showAlert() {
        AlertDialog.Builder(this).setTitle("Incomplete Entries").setMessage("Please fill in empty fields")
            .setPositiveButton("OK", null).show()
    }
}

