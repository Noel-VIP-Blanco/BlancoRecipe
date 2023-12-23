package com.blanco.noelscookbook.recyclerviewadapters


// IngredientAdapter.kt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blanco.noelscookbook.databinding.ItemIngredientBinding
import com.blanco.noelscookbook.room.Ingredient

class IngredientAdapterDisplayOnly(private val ingredientList: MutableList<Ingredient>) :
    RecyclerView.Adapter<IngredientAdapterDisplayOnly.IngredientViewHolder>() {

    class IngredientViewHolder(private val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: Ingredient) {
            binding.apply {
                textViewIngredientName.text = ingredient.name
                textViewIngredientQuantity.text = ingredient.quantity.toString()
                textViewIngredientUnit.text = ingredient.unit
                ingredientMenuImageButton.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIngredientBinding.inflate(inflater, parent, false)
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(ingredientList[position])
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }
}

