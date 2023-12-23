package com.blanco.noelscookbook.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface IngredientDao {
    @Insert
    suspend fun insert(ingredient: Ingredient)

    @Query("SELECT * FROM ingredient_table")
    suspend fun getAllIngredients(): List<Ingredient>

    @Query("DELETE FROM ingredient_table WHERE recipeId = :recipeId")
    suspend fun deleteIngredientsByRecipeId(recipeId: Long)

    @Query("SELECT * FROM ingredient_table WHERE recipeId = :recipeId")
    suspend fun getIngredientsByRecipeId(recipeId: Long): List<Ingredient>

    @Update
    suspend fun update(ingredient: Ingredient)

    @Delete
    suspend fun delete(ingredient: Ingredient)


}
