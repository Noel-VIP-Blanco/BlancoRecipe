package com.blanco.noelscookbook.bottomsheetfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blanco.noelscookbook.R
import com.blanco.noelscookbook.databinding.AddIngredientBottomSheetLayoutBinding
import com.blanco.noelscookbook.room.Ingredient
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddIngredientBottomSheetDialogFragment(
    private val recipeId: Long,
    private val ingredient: Ingredient?,
    private val onUpdate: (Ingredient) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: AddIngredientBottomSheetLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddIngredientBottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Set initial cooking step text if available
        ingredient?.let {
            binding.editTextIngredientName.setText(it.name)
            binding.editTextQuantity.setText(it.quantity.toString())
            // Find the index of the unit in the array and set it as the selected item
            val unitArray = resources.getStringArray(R.array.all_measurements)
            val unitIndex = unitArray.indexOf(it.unit)
            if (unitIndex != -1) {
                binding.spinnerUnit.setSelection(unitIndex)
            }
        }

        // Set click listener for the "Confirm" button
        binding.btnConfirm.setOnClickListener {
            // Retrieve entered data
            val name = binding.editTextIngredientName.text.toString()
            val quantityText = binding.editTextQuantity.text.toString()
            val unit = binding.spinnerUnit.selectedItem.toString()

            // Check if quantity is not empty
            if (quantityText.isNotEmpty()) {
                // Convert quantity to Double
                val quantity = quantityText.toDouble()

                // Create an Ingredient object with entered data and associated recipeId
                val updatedIngredient = Ingredient(
                    name = name,
                    quantity = quantity,
                    unit = unit,
                    recipeId = recipeId
                )

                // Pass the Ingredient back to the activity
                onUpdate(updatedIngredient)

                // Dismiss the bottom sheet dialog
                dismiss()
            } else {
                // Handle the case where quantity is empty (show an error, prompt the user, etc.)
                // For simplicity, you can add a toast message or log an error.
            }
        }
    }
}
