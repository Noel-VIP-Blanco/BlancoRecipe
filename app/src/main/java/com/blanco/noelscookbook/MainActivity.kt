package com.blanco.noelscookbook

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blanco.noelscookbook.databinding.ActivityMainBinding
import com.blanco.noelscookbook.recyclerviewadapters.RecipeAdapter
import com.blanco.noelscookbook.repositories.RecipeRepository
import com.blanco.noelscookbook.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var recipeAdapter: RecipeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        recipeRepository = RecipeRepository(AppDatabase.getDatabase(this).recipeDao())
        // Fetch all recipes from the database and display them
        displayAllRecipes()

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddARecipeActivity::class.java))







        }
    }

    override fun onResume() {
        super.onResume()
        displayAllRecipes()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayAllRecipes() {
        lifecycleScope.launch(Dispatchers.Main) {
            val allRecipes = recipeRepository.getAllRecipes()

            // Log the size of the list
            Log.d("RecipeListSize", "Recipe list size: ${allRecipes.size}")

            // Assuming you have a RecyclerView and an adapter set up in your layout
            // Update the adapter with the list of recipes
            recipeAdapter = RecipeAdapter(allRecipes.toMutableList(), recipeRepository, this@MainActivity)
            binding.recipeRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.recipeRecyclerView.adapter = recipeAdapter
            recipeAdapter.notifyDataSetChanged()

            // Update the visibility of emptyImageView and emptyTextView
            updateEmptyViewVisibility(allRecipes.isEmpty())
        }
    }

    fun updateEmptyViewVisibility(isEmpty: Boolean) {
        binding.emptyTextView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.emptyImageView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }


}
