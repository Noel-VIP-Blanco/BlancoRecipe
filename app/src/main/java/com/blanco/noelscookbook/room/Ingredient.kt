// Ingredient.kt

package com.blanco.noelscookbook.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingredient_table",
    foreignKeys = [ForeignKey(
        entity = Recipe::class,
        parentColumns = ["id"],
        childColumns = ["recipeId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var recipeId: Long, // Foreign key referencing Recipe
    val name: String,
    val quantity: Double,
    val unit: String

)
