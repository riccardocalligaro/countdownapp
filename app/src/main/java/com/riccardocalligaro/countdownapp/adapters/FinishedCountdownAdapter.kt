package com.riccardocalligaro.countdownapp.adapters

import android.content.Context
import android.graphics.Color
import android.net.Uri
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
import com.riccardocalligaro.countdownapp.utils.Costants.TRUE
import com.riccardocalligaro.countdownapp.utils.Methods
import com.riccardocalligaro.countdownapp.utils.Methods.Companion.getFormattedDate
import com.riccardocalligaro.countdownapp.utils.Methods.Companion.getFormattedDateForEditNew
import com.riccardocalligaro.countdownapp.utils.Methods.Companion.setTextColor
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.recycler_view_item_expanded_bg.view.*
import kotlinx.android.synthetic.main.recycler_view_item_finished.view.*
import kotlinx.android.synthetic.main.recycler_view_item_normal_bg.view.*

class FinishedCountdownAdapter(
    val context: Context,
    val countdowns: List<Countdown>,
    val clickListener: (Int) -> Unit,
    val longClickListener: (Int) -> Unit
) :
    RecyclerView.Adapter<FinishedCountdownAdapter.FinishedCountdownViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedCountdownViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_finished, parent, false)
        return FinishedCountdownViewHolder(v)
    }

    override fun onBindViewHolder(holder: FinishedCountdownViewHolder, position: Int) {
        val countdown = countdowns[position]
        holder.bind(countdown)
        holder.clickableView?.setOnClickListener {
            clickListener(position)
        }

        holder.clickableView?.setOnLongClickListener {
            longClickListener(position)
            true
        }
    }

    class FinishedCountdownViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val clickableView = itemView

        private fun getContext(): Context {
            return itemView.context
        }

        fun bind(countdown: Countdown) {

            itemView.recycler_view_main_finished_event_name.text = countdown.name
            itemView.recycler_view_finished_main_event_date.text = getFormattedDateForEditNew(countdown.date)

            setTextColor(itemView, countdown)
            if (countdown.imageBg == TRUE || countdown.customColoredWallpaper == TRUE) {
                itemView.main_recycler_view_card_finish_bg_image.visibility = View.VISIBLE
                setImageBackground(itemView, countdown)

            }
        }

        private fun setTextColor(itemView: View, countdown: Countdown) {
            val color = Color.parseColor(countdown.color)
            itemView.recycler_view_main_finished_event_name.setTextColor(color)
            itemView.recycler_view_finished_main_event_date.setTextColor(color)
            itemView.recycler_view_main_finished_icon.setColorFilter(color)
        }

        private fun setImageBackground(itemView: View, countdown: Countdown) {
            if (countdown.imageBg == TRUE) {
                val uri = Uri.parse(countdown.imagePath)

                Glide.with(getContext())
                    .load(uri)
                    .thumbnail(0.1f)
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(15, 2)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(itemView.main_recycler_view_card_finish_bg_image)
            } else {
                itemView.main_recycler_view_card_finish_bg_image.setBackgroundColor(Color.parseColor(countdown.wallpaperColor))
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
        return super.getItemViewType(position)
    }


}