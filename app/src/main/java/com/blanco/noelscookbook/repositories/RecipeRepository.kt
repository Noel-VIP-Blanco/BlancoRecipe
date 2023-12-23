// RecipeRepository.kt

package com.blanco.noelscookbook.repositories

import com.blanco.noelscookbook.room.Recipe
import com.blanco.noelscookbook.room.RecipeDao

class RecipeRepository(private val recipeDao: RecipeDao) {

    // Suspend function to insert a recipe and return its generated ID
    suspend fun insert(recipe: Recipe): Long {
        return recipeDao.insert(recipe)
    }

    // Suspend function to get all recipes
    suspend fun getAllRecipes(): List<Recipe> {
        return recipeDao.getAllRecipes()
    }

    // Suspend function to delete a recipe and its associated data
    suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipeAndRelatedData(recipe.id)
    }

    suspend fun getRecipeById(recipeId: Long): Recipe {
        return recipeDao.getRecipeById(recipeId)
    }

    suspend fun update(recipe: Recipe) {
        recipeDao.update(recipe)
    }
}
