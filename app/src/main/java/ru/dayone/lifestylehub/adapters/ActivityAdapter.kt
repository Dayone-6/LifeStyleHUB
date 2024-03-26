package ru.dayone.lifestylehub.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.data.local.AppPrefs
import ru.dayone.lifestylehub.data.local.activity.ActivityEntity

class ActivityAdapter(
    private var activities: MutableList<ActivityEntity>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvActivity: TextView = view.findViewById(R.id.tv_activity_value)
        val tvPrice: TextView = view.findViewById(R.id.tv_activity_price)
        val tvAccess: TextView = view.findViewById(R.id.tv_activity_accessibility)
        val tvType: TextView = view.findViewById(R.id.tv_activity_type)
        val ivFav: ImageView = view.findViewById(R.id.iv_activity_fav)
        val cvRoot: CardView = view.findViewById(R.id.cv_activity_root)
        val tvParticipants: TextView = view.findViewById(R.id.tv_activity_pariticipants)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_activity, parent, false)
        )
    }

    override fun getItemCount(): Int = activities.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = activities[position]

        holder.tvActivity.text = item.activity
        holder.tvAccess.text = item.accessibility.toString()
        holder.tvPrice.text = item.price.toString()
        holder.tvType.text = item.type
        holder.tvParticipants.text = item.participants.toString()

        if(item.isFavourite){
            holder.ivFav.setImageResource(R.drawable.ic_heart_filled)
        }else{
            holder.ivFav.setImageResource(R.drawable.ic_heart_outline)
        }

        holder.cvRoot.setOnClickListener {
            onClickListener.onItemClick(item)
        }

        holder.ivFav.setOnClickListener {
            if(AppPrefs.getIsAuthorized()) {
                item.isFavourite = !item.isFavourite
                if (item.isFavourite) {
                    holder.ivFav.setImageResource(R.drawable.ic_heart_filled)
                } else {
                    holder.ivFav.setImageResource(R.drawable.ic_heart_outline)
                }
            }
            onClickListener.onFavIconClick(item)
        }
    }

    fun addActivity(activity: ActivityEntity){
        activities.reverse()
        activities.add(activity)
        activities.reverse()
        notifyItemInserted(0)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun replaceData(newData: MutableList<ActivityEntity>){
        activities = newData
        notifyDataSetChanged()
    }

    fun getData() = activities

    interface OnClickListener{
        fun onItemClick(item: ActivityEntity)
        fun onFavIconClick(item: ActivityEntity)
    }

}