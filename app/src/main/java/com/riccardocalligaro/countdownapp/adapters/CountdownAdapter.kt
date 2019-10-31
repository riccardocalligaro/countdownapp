package com.riccardocalligaro.countdownapp.adapters

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.riccardocalligaro.countdownapp.R
import com.riccardocalligaro.countdownapp.db.entities.Countdown
import com.riccardocalligaro.countdownapp.utils.Costants.FALSE
import com.riccardocalligaro.countdownapp.utils.Costants.TRUE
import com.riccardocalligaro.countdownapp.utils.Methods.Companion.countdownSetExpanded
import com.riccardocalligaro.countdownapp.utils.Methods.Companion.countdownSetNormal
import com.riccardocalligaro.countdownapp.utils.Methods.Companion.getFormattedDate
import com.riccardocalligaro.countdownapp.utils.Methods.Companion.setTextColor
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.recycler_view_item_expanded.view.*
import kotlinx.android.synthetic.main.recycler_view_item_expanded_bg.view.*
import kotlinx.android.synthetic.main.recycler_view_item_normal.view.*
import kotlinx.android.synthetic.main.recycler_view_item_normal_bg.view.*


class CountdownAdapter(
    val context: Context,
    val countdowns: List<Countdown>,
    val clickListener: (Int) -> Unit,
    val longClickListener: (Int) -> Unit
) :
    RecyclerView.Adapter<CountdownAdapter.CountdownViewHolder>() {


    companion object {
        private const val VIEW_TYPE_NORMAL = 18
        private const val VIEW_TYPE_EXTENDED = 20
        private const val VIEW_TYPE_NORMAL_BG = 21
        private const val VIEW_TYPE_EXTENDED_BG = 23
        private const val VIEW_TYPE_FINISHED = 24
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountdownViewHolder {

        val mContext = parent.context

        Log.i("viewType", viewType.toString())
        val view = when (viewType) {
            VIEW_TYPE_NORMAL -> LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_view_item_normal,
                parent,
                false
            )
            VIEW_TYPE_EXTENDED -> LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_view_item_expanded,
                parent,
                false
            )
            VIEW_TYPE_NORMAL_BG -> LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_view_item_normal_bg,
                parent,
                false
            )
            VIEW_TYPE_EXTENDED_BG -> LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_view_item_expanded_bg,
                parent,
                false
            )
            VIEW_TYPE_FINISHED -> LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_view_item_gone,
                parent,
                false
            )
            else -> LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_view_item_normal,
                parent,
                false
            )
        }



        return CountdownViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountdownViewHolder, position: Int) {
        val countdown = countdowns[position]
        holder.bind(countdown)
        (holder as CountdownViewHolder).clickableView?.setOnClickListener {
            clickListener(position)
        }

        (holder as CountdownViewHolder).clickableView?.setOnLongClickListener {
            longClickListener(position)
            true
        }


    }


    class CountdownViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val clickableView = itemView

        private fun getContext(): Context {
            return itemView.context
        }

        fun bind(item: Countdown) {
            setTextColor(itemView, item)
            if (item.expand == TRUE && item.imageBg == FALSE && item.customColoredWallpaper == FALSE && item.finished != TRUE) {
                //VIEW_TYPE_EXTENDED
                expandedNoBackground(itemView, item)
            } else if (item.expand == FALSE && item.imageBg == FALSE && item.customColoredWallpaper == FALSE && item.finished != TRUE) {
                hideProgressBar(item, itemView)
                normalNoBackground(itemView, item)
            } else if (item.expand == TRUE && item.imageBg == TRUE && item.finished != TRUE || item.expand == TRUE && item.customColoredWallpaper == TRUE && item.finished != TRUE) {
                expandedBackground(itemView, item)
                normalImageBackground(itemView, item)
            } else if (item.finished != TRUE) {
                normalBackground(itemView, item)
                normalImageBackground(itemView, item)
                hideProgressBar(item, itemView)
            } else {
                //finished
            }

        }

        private fun expandedNoBackground(itemView: View, item: Countdown) {
            itemView.recycler_view_main_expanded_event_name.text = item.name
            itemView.recycler_view_main_expanded_event_date.text = getFormattedDate(item.date)
            countdownSetExpanded(itemView, item)
        }

        private fun expandedBackground(itemView: View, item: Countdown) {
            itemView.recycler_view_main_expanded_bg_event_name.text = item.name
            itemView.recycler_view_main_expanded_bg_event_date.text = getFormattedDate(item.date)
            countdownSetExpanded(itemView, item)
        }

        private fun normalNoBackground(itemView: View, item: Countdown) {
            itemView.recycler_view_main_event_name.text = item.name
            itemView.recycler_view_main_event_date.text = getFormattedDate(item.date)
            countdownSetNormal(itemView, item)
        }

        private fun normalBackground(itemView: View, item: Countdown) {
            itemView.recycler_view_main_bg_event_name?.text = item.name
            itemView.recycler_view_main_bg_event_date.text = getFormattedDate(item.date)

            countdownSetNormal(itemView, item)
        }

        private fun hideProgressBar(countdown: Countdown, itemView: View) {

            if (countdown.imageBg == TRUE || countdown.customColoredWallpaper == TRUE) {
                itemView.progressBarNormalBg.visibility = View.GONE
            } else {
                itemView.progressBarItemNormal.visibility = View.GONE
            }
        }

        private fun normalImageBackground(itemView: View, item: Countdown) {

            if (item.imageBg == TRUE) {
                val uri = Uri.parse(item.imagePath)
                var imageView: ImageView? = null
                imageView = if (item.expand == TRUE) {
                    itemView.recycler_view_main_expanded_bg_image
                } else {
                    itemView.main_recycler_view_card_normal_bg_background_image
                }

                Glide.with(getContext())
                    .load(uri)
                    .thumbnail(0.1f)
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(15, 2)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(imageView)

            } else {
                if (item.expand == TRUE) {
                    itemView.recycler_view_main_expanded_bg_image.setBackgroundColor(Color.parseColor(item.wallpaperColor))
                } else {
                    itemView.main_recycler_view_card_normal_bg_background_image.setBackgroundColor(Color.parseColor(item.wallpaperColor))
                }
            }

        }
    }


    override fun getItemCount(): Int {
        return countdowns.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    override fun getItemViewType(position: Int): Int {


        //if (countdowns[position].expand == TRUE && countdowns[position].imageBg == FALSE )
        return if (countdowns[position].expand == TRUE && countdowns[position].imageBg == FALSE && countdowns[position].customColoredWallpaper == FALSE && countdowns[position].finished != TRUE) {
            VIEW_TYPE_EXTENDED
            Log.i("position", position.toString() + "EXTENDED")
        } else if (countdowns[position].expand == FALSE && countdowns[position].imageBg == FALSE && countdowns[position].customColoredWallpaper == FALSE && countdowns[position].finished != TRUE) {
            VIEW_TYPE_NORMAL
            Log.i("position", position.toString() + "NORMAL")
        } else if (countdowns[position].expand == TRUE && countdowns[position].imageBg == TRUE && countdowns[position].finished != TRUE || countdowns[position].expand == TRUE && countdowns[position].customColoredWallpaper == TRUE && countdowns[position].finished != TRUE) {
            VIEW_TYPE_EXTENDED_BG
            Log.i("position", position.toString() + "EXTENDED_BG")
        } else if (countdowns[position].finished != TRUE) {
            VIEW_TYPE_NORMAL_BG
            Log.i("position", position.toString() + "NORMAL_BG")
        } else {
            VIEW_TYPE_FINISHED
        }

    }


}