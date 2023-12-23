package com.blanco.noelscookbook.bottomsheetfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blanco.noelscookbook.databinding.AddCookingStepBottomSheetLayoutBinding
import com.blanco.noelscookbook.room.CookingStep
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddCookingStepBottomSheetDialogFragment(
    private val recipeId: Long,
    private val cookingStep: CookingStep?,
    private val onUpdate: (CookingStep) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: AddCookingStepBottomSheetLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddCookingStepBottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set initial cooking step text if available
        cookingStep?.let {
            binding.editTextCookingStep.setText(it.description)
        }

        // Set click listener for the "Confirm" button
        binding.btnConfirmCookingStep.setOnClickListener {
            // Retrieve entered cooking step
            val cookingStepText = binding.editTextCookingStep.text.toString()

            // Check if cooking step is not empty
            if (cookingStepText.isNotEmpty()) {
                // Create a CookingStep object with entered data and associated recipeId
                val updatedCookingStep = CookingStep(
                    id = cookingStep?.id ?: 0,
                    description = cookingStepText,
                    recipeId = recipeId
                )

                // Pass the updated CookingStep back to the activity
                onUpdate(updatedCookingStep)

                // Dismiss the bottom sheet dialog
                dismiss()
            } else {
                // Handle the case where cooking step is empty (show an error, prompt the user, etc.)
                // For simplicity, you can add a toast message or log an error.
            }
        }
    }


}
