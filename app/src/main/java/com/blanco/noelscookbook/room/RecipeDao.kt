// RecipeDao.kt

package com.blanco.noelscookbook.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface RecipeDao {
    @Insert
    suspend fun insert(recipe: Recipe): Long

    @Query("SELECT * FROM recipe_table")
    suspend fun getAllRecipes(): List<Recipe>

    @Transaction
    @Query("DELETE FROM recipe_table WHERE id = :recipeId")
    suspend fun deleteRecipeAndRelatedData(recipeId: Long)

    @Query("SELECT * FROM recipe_table WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Long): Recipe

    @Update
    suspend fun update(recipe: Recipe)
}
