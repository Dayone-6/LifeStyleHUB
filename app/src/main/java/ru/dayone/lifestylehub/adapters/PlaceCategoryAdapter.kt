package ru.dayone.lifestylehub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.dayone.lifestylehub.R

class PlaceCategoryAdapter(
    private var categories: List<String>
) : RecyclerView.Adapter<PlaceCategoryAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvCategory: TextView = view.findViewById(R.id.tv_category_name)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCategory.text = categories[position]
    }

    override fun getItemCount(): Int = categories.size

    fun replaceData(newCategories: List<String>){
        this.categories = newCategories
        notifyItemRangeChanged(0, newCategories.size)
    }
}