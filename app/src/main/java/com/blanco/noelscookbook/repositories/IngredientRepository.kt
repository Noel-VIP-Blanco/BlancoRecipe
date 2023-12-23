package com.blanco.noelscookbook.repositories

import com.blanco.noelscookbook.room.Ingredient
import com.blanco.noelscookbook.room.IngredientDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IngredientRepository(private val dao: IngredientDao) {

    suspend fun getAllIngredients(): List<Ingredient> = withContext(Dispatchers.IO) {
        try {
            dao.getAllIngredients()
        } catch (e: Exception) {
            // Handle the exception (e.g., log, show a toast, etc.)
            emptyList()
        }
    }

    suspend fun insert(ingredient: Ingredient) {
        withContext(Dispatchers.IO) {
            try {
                dao.insert(ingredient)
            } catch (e: Exception) {
                // Handle the exception (e.g., log, show a toast, etc.)
            }
        }
    }

    suspend fun getIngredientsByRecipeId(recipeId: Long): List<Ingredient> {
        return dao.getIngredientsByRecipeId(recipeId)
    }

    suspend fun update(ingredient: Ingredient) {
        dao.update(ingredient)
    }

    suspend fun delete(ingredient: Ingredient) {
        dao.delete(ingredient)
    }



}
