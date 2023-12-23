package com.blanco.noelscookbook.recyclerviewadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blanco.noelscookbook.databinding.ItemCookingStepBinding
import com.blanco.noelscookbook.room.CookingStep

class CookingStepAdapterDisplayOnly(private val cookingStepList: MutableList<CookingStep>) :
    RecyclerView.Adapter<CookingStepAdapterDisplayOnly.CookingStepViewHolder>() {

    class CookingStepViewHolder(private val binding: ItemCookingStepBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cookingStep: CookingStep) {
            binding.textViewCookingStep.text = cookingStep.description
            binding.cookingStepMenuImageButton.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookingStepViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCookingStepBinding.inflate(inflater, parent, false)
        return CookingStepViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CookingStepViewHolder, position: Int) {
        holder.bind(cookingStepList[position])
    }

    override fun getItemCount(): Int {
        return cookingStepList.size
    }
}
