// CookingStepDao.kt

package com.blanco.noelscookbook.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CookingStepDao {
    @Insert
    suspend fun insert(cookingStep: CookingStep)

    @Query("SELECT * FROM cooking_step_table")
    suspend fun getAllCookingSteps(): List<CookingStep>

    @Query("DELETE FROM cooking_step_table WHERE recipeId = :recipeId")
    suspend fun deleteCookingStepsByRecipeId(recipeId: Long)

    @Query("SELECT * FROM cooking_step_table WHERE recipeId = :recipeId")
    suspend fun getCookingStepsByRecipeId(recipeId: Long): List<CookingStep>

    @Update
    suspend fun update(cookingStep: CookingStep)

    @Delete
    suspend fun delete(cookingStep: CookingStep)

}
