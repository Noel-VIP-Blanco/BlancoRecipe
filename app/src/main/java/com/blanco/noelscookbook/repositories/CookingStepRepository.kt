// CookingStepRepository.kt

package com.blanco.noelscookbook.repositories

import com.blanco.noelscookbook.room.CookingStep
import com.blanco.noelscookbook.room.CookingStepDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CookingStepRepository(private val dao: CookingStepDao) {

    suspend fun insert(cookingStep: CookingStep) {
        withContext(Dispatchers.IO) {
            dao.insert(cookingStep)
        }
    }

    suspend fun getCookingStepsByRecipeId(recipeId: Long): List<CookingStep> {
        return dao.getCookingStepsByRecipeId(recipeId)
    }

    suspend fun update(cookingStep: CookingStep) {
        dao.update(cookingStep)
    }

    suspend fun delete(cookingStep: CookingStep) {
        dao.delete(cookingStep)
    }
}
