package ru.dayone.lifestylehub.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.data.local.leisure.LeisureEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LeisureAdapter(
    private var leisure: MutableList<LeisureEntity>,
    private val onAction: ActionListener
) : RecyclerView.Adapter<LeisureAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_leisure_title)
        val tvDate: TextView = view.findViewById(R.id.tv_leisure_date)
        val root: CardView = view.findViewById(R.id.cv_leisure_root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_leisure, parent, false)
        )
    }

    override fun getItemCount(): Int = leisure.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = leisure[position]

        holder.tvTitle.text = item.title
        holder.tvDate.text =
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(item.date))

        holder.root.setOnLongClickListener {
            onAction.onDelete(item)
            true
        }

        holder.root.setOnClickListener {
            onAction.onItemClick(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun replaceData(newData: List<LeisureEntity>) {
        leisure.clear()
        leisure.addAll(newData)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(id: Int) {
        leisure.removeIf { it.id == id }
        notifyDataSetChanged()
    }

    interface ActionListener {
        fun onDelete(item: LeisureEntity)

        fun onItemClick(item: LeisureEntity)
    }
}