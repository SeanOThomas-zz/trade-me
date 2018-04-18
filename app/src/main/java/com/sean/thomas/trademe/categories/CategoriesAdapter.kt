package com.sean.thomas.trademe.categories

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sean.thomas.trademe.R
import com.sean.thomas.trademe.network.models.Category
import kotlinx.android.synthetic.main.item_category.view.*

class CategoriesAdapter(
        private val onCategoryClicked: (category: Category) -> Unit
): RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private var categories: List<Category> = ArrayList()

    class CategoryViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val category: TextView = v.category
        val count: TextView = v.count
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category, parent, false))
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        holder.category.text = category.name

        val formattedCount = holder.itemView.context.getString(R.string.category_count, category.count)
        holder.count.text = formattedCount

        holder.itemView.setOnClickListener({onCategoryClicked(category)})
    }

    fun setCategories(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }
}