package com.blanco.noelscookbook.recyclerviewadapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.blanco.noelscookbook.R
import com.blanco.noelscookbook.bottomsheetfragments.AddCookingStepBottomSheetDialogFragment
import com.blanco.noelscookbook.databinding.ItemCookingStepBinding
import com.blanco.noelscookbook.room.CookingStep

class CookingStepAdapter(
    private val cookingStepList: MutableList<CookingStep>
) : RecyclerView.Adapter<CookingStepAdapter.CookingStepViewHolder>() {

    class CookingStepViewHolder(
        private val binding: ItemCookingStepBinding,
        private val adapter: CookingStepAdapter
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.cookingStepMenuImageButton.setOnClickListener(this)
        }

        fun bind(cookingStep: CookingStep) {
            binding.textViewCookingStep.text = cookingStep.description
        }

        override fun onClick(view: View?) {
            if (view?.id == R.id.cookingStepMenuImageButton) {
                showPopupMenu(view)
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun showPopupMenu(view: View) {
            val popupMenu = PopupMenu(view.context, view)
            val inflater: MenuInflater = popupMenu.menuInflater
            inflater.inflate(R.menu.popmenu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_update -> {
                        // Show the bottom sheet dialog for updating
                        showBottomSheetDialogForUpdate(adapterPosition)
                        true
                    }
                    R.id.menu_delete -> {
                        // Handle delete action
                        adapter.cookingStepList.removeAt(adapterPosition)
                        adapter.notifyDataSetChanged()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        private fun showBottomSheetDialogForUpdate(position: Int) {
            val cookingStep = adapter.cookingStepList[position]

            val bottomSheetFragment = AddCookingStepBottomSheetDialogFragment(
                recipeId = cookingStep.recipeId,
                cookingStep = cookingStep,
                onUpdate = { updatedCookingStep ->
                    // Handle the updated cooking step
                    adapter.cookingStepList[position] = updatedCookingStep
                    adapter.notifyDataSetChanged()
                }
            )

            bottomSheetFragment.show(
                (itemView.context as AppCompatActivity).supportFragmentManager,
                bottomSheetFragment.tag
            )
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookingStepViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCookingStepBinding.inflate(inflater, parent, false)
        return CookingStepViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: CookingStepViewHolder, position: Int) {
        val currentCookingStep = cookingStepList[position]
        holder.bind(currentCookingStep)
    }

    override fun getItemCount(): Int {
        return cookingStepList.size
    }


}

