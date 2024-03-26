package ru.dayone.lifestylehub.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.faltenreich.skeletonlayout.createSkeleton
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.data.local.AppPrefs
import ru.dayone.lifestylehub.ui.home.HomeViewModel
import ru.dayone.lifestylehub.utils.DATE_KEY
import ru.dayone.lifestylehub.utils.FeedItem
import ru.dayone.lifestylehub.utils.FeedItemType
import ru.dayone.lifestylehub.utils.PAGINATION_LIMIT
import ru.dayone.lifestylehub.utils.PLACES_OAUTH_KEY
import java.lang.ClassCastException

class FeedAdapter(
    private var feedItems: List<FeedItem>,
    private val context: Context,
    private val viewModel: HomeViewModel,
    private val onItemClick: OnItemClick
) : RecyclerView.Adapter<ViewHolder>() {

    interface OnItemClick{
        fun onClick(item: FeedItem.Place)
    }

    private class DefaultViewHolder(view: View) : ViewHolder(view)
    private class PlaceViewHolder(view: View) : ViewHolder(view) {
        val ivPhoto: ImageView = view.findViewById(R.id.iv_place_photo)
        val tvName: TextView = view.findViewById(R.id.tv_place_name)
        val tvAddress: TextView = view.findViewById(R.id.tv_place_address)
        val rvCategories: RecyclerView = view.findViewById(R.id.rv_place_categories)
        val cvPlaceRoot: CardView = view.findViewById(R.id.cv_place_root)
    }

    private class PagingControlViewHolder(view: View) : ViewHolder(view) {
        val tvLoadMore: TextView = view.findViewById(R.id.tv_paging_load)
        val progress: ProgressBar = view.findViewById(R.id.pb_paging)
        val tvEnd: TextView = view.findViewById(R.id.tv_pading_end)
    }

    override fun getItemViewType(position: Int): Int {
        return feedItems[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            FeedItemType.Place.ordinal -> {
                PlaceViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_place, parent, false)
                )
            }

            FeedItemType.Paging.ordinal -> {
                PagingControlViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_paging, parent, false)
                )
            }

            else -> {
                DefaultViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_default, parent, false)
                )
            }
        }
    }

    override fun getItemCount(): Int = feedItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feedItem = feedItems[position]
        when (feedItem.type) {
            FeedItemType.Place.ordinal -> {
                bindPlaceViewHolder(holder as PlaceViewHolder, feedItem as FeedItem.Place)
            }

            FeedItemType.Paging.ordinal -> {
                bingPagingViewHolder(holder as PagingControlViewHolder)
            }
        }
    }

    private fun bingPagingViewHolder(holder: PagingControlViewHolder) {
        holder.tvLoadMore.visibility = View.VISIBLE
        holder.progress.visibility = View.GONE
        holder.tvEnd.visibility = View.GONE
        try {
            if ((feedItems[0] as FeedItem.Place).place.allCount > feedItems.size) {
                holder.tvLoadMore.setOnClickListener {
                    holder.progress.visibility = View.VISIBLE
                    holder.tvLoadMore.visibility = View.GONE
                    val location = AppPrefs.getLocation()
                    viewModel.getPlaces(
                        PLACES_OAUTH_KEY,
                        "${location!!.latitude},${location.longitude}",
                        DATE_KEY,
                        PAGINATION_LIMIT,
                        feedItems.size - 1
                    )
                }
            } else {
                holder.tvLoadMore.visibility = View.GONE
                holder.progress.visibility = View.GONE
                holder.tvEnd.visibility = View.VISIBLE
            }
        }catch (ignored: ClassCastException){
            holder.tvLoadMore.visibility = View.GONE
            holder.progress.visibility = View.GONE
            holder.tvEnd.visibility = View.GONE
        }
    }

    private fun bindPlaceViewHolder(holder: PlaceViewHolder, feedItem: FeedItem.Place) {
        val photoSkeleton = holder.ivPhoto.createSkeleton()
        if (holder.ivPhoto.drawable == null) {
            photoSkeleton.showSkeleton()
        }
        Glide
            .with(context)
            .load(feedItem.place.photoUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.ivPhoto.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    photoSkeleton.showOriginal()
                    return false
                }
            })
            .into(holder.ivPhoto)

        holder.tvName.text = feedItem.place.name
        holder.tvAddress.text = feedItem.place.address

        holder.rvCategories.adapter = PlaceCategoryAdapter(feedItem.place.categories)
        holder.rvCategories.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        holder.cvPlaceRoot.setOnClickListener {
            onItemClick.onClick(feedItem)
        }

    }

    fun replaceData(data: List<FeedItem>){
        feedItems = data
        notifyItemRangeChanged(0, data.size)
    }
}