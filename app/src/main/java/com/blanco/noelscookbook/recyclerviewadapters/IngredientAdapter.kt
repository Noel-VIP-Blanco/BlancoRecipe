package com.blanco.noelscookbook.recyclerviewadapters

// IngredientAdapter.kt
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.blanco.noelscookbook.R
import com.blanco.noelscookbook.bottomsheetfragments.AddIngredientBottomSheetDialogFragment
import com.blanco.noelscookbook.databinding.ItemIngredientBinding
import com.blanco.noelscookbook.room.Ingredient

class IngredientAdapter(private val ingredientList: MutableList<Ingredient>) :
    RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    class IngredientViewHolder(
        private val binding: ItemIngredientBinding,
        private val adapter: IngredientAdapter) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.ingredientMenuImageButton.setOnClickListener(this)
        }

        fun bind(ingredient: Ingredient) {
            binding.apply {
                textViewIngredientName.text = ingredient.name
                textViewIngredientQuantity.text = ingredient.quantity.toString()
                textViewIngredientUnit.text = ingredient.unit

            }
        }

        override fun onClick(view: View?) {
            if (view?.id == R.id.ingredientMenuImageButton) {
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
                        // Handle update action
                        showBottomSheetDialogForUpdate(adapterPosition)
                        true
                    }

                    R.id.menu_delete -> {
                        // Handle delete action
                        adapter.ingredientList.removeAt(adapterPosition)
                        adapter.notifyDataSetChanged()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        private fun showBottomSheetDialogForUpdate(position: Int) {
            val ingredientInstance = adapter.ingredientList[position]

            val bottomSheetFragment = AddIngredientBottomSheetDialogFragment(
                recipeId = ingredientInstance.recipeId,
                ingredient = ingredientInstance,
                onUpdate = { updatedIngredients ->
                    // Handle the updated cooking step
                    adapter.ingredientList[position] = updatedIngredients
                    adapter.notifyDataSetChanged()
                }
            )

            bottomSheetFragment.show(
                (itemView.context as AppCompatActivity).supportFragmentManager,
                bottomSheetFragment.tag
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIngredientBinding.inflate(inflater, parent, false)
        return IngredientViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(ingredientList[position])
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }
}
