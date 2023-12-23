package com.blanco.noelscookbook.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_table")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
    var servings: Int,
    var cookingTime: Int,
    var cuisineType: String
)

