package com.blanco.noelscookbook.recyclerviewadapters
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.blanco.noelscookbook.MainActivity
import com.blanco.noelscookbook.R
import com.blanco.noelscookbook.RecipeDetailActivity
import com.blanco.noelscookbook.UpdateRecipeActivity
import com.blanco.noelscookbook.databinding.ItemRecipeBinding
import com.blanco.noelscookbook.repositories.RecipeRepository
import com.blanco.noelscookbook.room.Recipe
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeAdapter(
    private var recipes: MutableList<Recipe>,
    private var recipeRepository: RecipeRepository,
    private var mainActivity: MainActivity
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {


    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.recipeMenuImageButton.setOnClickListener {
                showPopupMenu()
            }
        }

        override fun onClick(view: View?) {
            // Redirect to another activity when the item is clicked
            val intent = Intent(binding.root.context, RecipeDetailActivity::class.java)
            intent.putExtra("recipeId", recipes[adapterPosition].id)
            intent.putExtra("recipeName", recipes[adapterPosition].name)
            // Add other data if needed
            binding.root.context.startActivity(intent)
        }

        fun bind(recipe: Recipe) {

            binding.textViewRecipeName.text = recipe.name
            // Bind other views if needed
        }

        private fun showPopupMenu() {
            val popupMenu = PopupMenu(binding.root.context, binding.recipeMenuImageButton)
            val inflater: MenuInflater = popupMenu.menuInflater
            inflater.inflate(R.menu.recipe_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_update -> {
                        val intent = Intent(binding.root.context, UpdateRecipeActivity::class.java)
                        intent.putExtra("recipeId", recipes[adapterPosition].id)  // Pass the recipe ID
                        intent.putExtra("recipeName", recipes[adapterPosition].name)  // Pass other recipe details
                        startActivity(binding.root.context, intent, null)
                        true
                    }
                    R.id.menu_delete -> {
                        showDeleteConfirmationDialog(adapterPosition)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        private fun showDeleteConfirmationDialog(position: Int) {
            AlertDialog.Builder(binding.root.context)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this recipe?")
                .setPositiveButton("Yes") { _, _ ->
                    onDeleteRecipe(position)
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun onDeleteRecipe(position: Int) {
        // Delete the recipe and associated data from the Room database
        GlobalScope.launch(Dispatchers.IO) {
            recipeRepository.deleteRecipe(recipes[position])

            // Remove the item from the list and notify the adapter
            recipes.removeAt(position)
            withContext(Dispatchers.Main) {
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, recipes.size)

                // Update the visibility of emptyImageView and emptyTextView
                mainActivity.runOnUiThread {
                    mainActivity.updateEmptyViewVisibility(recipes.isEmpty())
                }
            }
        }
    }







    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size



}
