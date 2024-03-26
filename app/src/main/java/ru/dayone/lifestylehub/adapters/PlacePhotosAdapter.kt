package ru.dayone.lifestylehub.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.faltenreich.skeletonlayout.createSkeleton
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.data.local.AppPrefs


class PlacePhotosAdapter(
    private val photos: List<String>,
    private val prefix: String
) : RecyclerView.Adapter<PlacePhotosAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.iv_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo, parent, false)
        )
    }

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val skeleton = holder.image.createSkeleton(AppPrefs.getSkeletonConfig())
        if(holder.image.drawable == null) {
            skeleton.showSkeleton()
        }
        Glide
            .with(holder.image)
            .load(prefix + photos[position])
            .addListener(object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.image.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    skeleton.showOriginal()
                    return false
                }

            }).into(holder.image)
    }
}