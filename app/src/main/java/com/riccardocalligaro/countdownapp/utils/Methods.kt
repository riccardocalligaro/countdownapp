package com.riccardocalligaro.countdownapp.utils

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseViewHolder
import com.riccardocalligaro.countdownapp.R
import com.riccardocalligaro.countdownapp.db.DBHelper
import com.riccardocalligaro.countdownapp.db.entities.Countdown
import com.riccardocalligaro.countdownapp.utils.Costants.FALSE
import com.riccardocalligaro.countdownapp.utils.Costants.TRUE
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.recycler_view_item_expanded.view.*
import kotlinx.android.synthetic.main.recycler_view_item_expanded_bg.view.*
import kotlinx.android.synthetic.main.recycler_view_item_finished.view.*
import kotlinx.android.synthetic.main.recycler_view_item_normal.view.*
import kotlinx.android.synthetic.main.recycler_view_item_normal_bg.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Methods {

    companion object {

        fun countdownSetNormal(itemView: View, countdown: Countdown) {
            val remainingMills = countdown.date.time - Calendar.getInstance().timeInMillis

            var diffInSecText = TimeUnit.MILLISECONDS.toSeconds(remainingMills)
            val seconds = diffInSecText % 60
            diffInSecText /= 60
            val minutes = diffInSecText % 60
            diffInSecText /= 60
            val hours = diffInSecText % 24
            diffInSecText /= 24
            val days = diffInSecText

            //Log.i("val-count", countdown.imagePath + "custom" + countdown.customColoredWallpaper)

            if (countdown.imageBg == TRUE || countdown.customColoredWallpaper == TRUE) {
                when {
                    days > 0 -> {
                        itemView.recycler_view_main_bg_event_remaining.text = days.toString()
                        itemView.recycler_view_mainbg__event_remaining_type.text = "days"
                    }
                    hours > 0 -> {
                        itemView.recycler_view_main_bg_event_remaining.text = hours.toString()
                        itemView.recycler_view_mainbg__event_remaining_type.text = "hours"
                    }
                    minutes > 0 -> {
                        itemView.recycler_view_main_bg_event_remaining.text = minutes.toString()
                        itemView.recycler_view_mainbg__event_remaining_type.text = "minutes"
                    }
                    seconds > 0 -> {
                        itemView.recycler_view_main_bg_event_remaining.text = seconds.toString()
                        itemView.recycler_view_mainbg__event_remaining_type.text = "seconds"
                    }
                }

            } else {

                when {
                    days > 0 -> {
                        itemView.recycler_view_main_event_remaining.text = days.toString()
                        itemView.recycler_view_main_event_remaining_type.text = "days"
                    }
                    hours > 0 -> {
                        itemView.recycler_view_main_event_remaining.text = hours.toString()
                        itemView.recycler_view_main_event_remaining_type.text = "hours"
                    }
                    minutes > 0 -> {
                        itemView.recycler_view_main_event_remaining.text = minutes.toString()
                        itemView.recycler_view_main_event_remaining_type.text = "minutes"
                    }
                    seconds > 0 -> {
                        itemView.recycler_view_main_event_remaining.text = seconds.toString()
                        itemView.recycler_view_main_event_remaining_type.text = "seconds"
                    }
                }
            }

            object : CountDownTimer(remainingMills, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    var mDiffInSeconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                    val mSeconds = mDiffInSeconds % 60
                    mDiffInSeconds /= 60
                    val mMinutes = mDiffInSeconds % 60
                    mDiffInSeconds /= 60
                    val mHours = mDiffInSeconds % 24
                    mDiffInSeconds /= 24
                    val mDays = mDiffInSeconds

                    if (countdown.imageBg == TRUE || countdown.customColoredWallpaper == TRUE) {
                        when {
                            mDays > 0 -> {
                                itemView.recycler_view_main_bg_event_remaining.text = mDays.toString()
                                itemView.recycler_view_mainbg__event_remaining_type.text = "days"
                            }
                            mHours > 0 -> {
                                itemView.recycler_view_main_bg_event_remaining.text = mHours.toString()
                                itemView.recycler_view_mainbg__event_remaining_type.text = "hours"
                            }
                            mMinutes > 0 -> {
                                itemView.recycler_view_main_bg_event_remaining.text = mMinutes.toString()
                                itemView.recycler_view_mainbg__event_remaining_type.text = "minutes"
                            }
                            mSeconds > 0 -> {
                                itemView.recycler_view_main_bg_event_remaining.text = mSeconds.toString()
                                itemView.recycler_view_mainbg__event_remaining_type.text = "seconds"
                            }
                        }

                    } else {

                        when {
                            mDays > 0 -> {
                                itemView.recycler_view_main_event_remaining.text = mDays.toString()
                                itemView.recycler_view_main_event_remaining_type.text = "days"
                            }
                            mHours > 0 -> {
                                itemView.recycler_view_main_event_remaining.text = mHours.toString()
                                itemView.recycler_view_main_event_remaining_type.text = "hours"
                            }
                            mMinutes > 0 -> {
                                itemView.recycler_view_main_event_remaining.text = mMinutes.toString()
                                itemView.recycler_view_main_event_remaining_type.text = "minutes"
                            }
                            mSeconds > 0 -> {
                                itemView.recycler_view_main_event_remaining.text = mSeconds.toString()
                                itemView.recycler_view_main_event_remaining_type.text = "seconds"
                            }
                        }
                    }


                }

                override fun onFinish() {
                    DBHelper.database.countdownDao().updateFinished(Costants.COUNTDOWN_FINISHED, countdown.id)


                }
            }.start()

        }

        fun countdownSetExpanded(itemView: View, item: Countdown) {
            val remainingMills = item.date.time - Calendar.getInstance().timeInMillis

            var diffInSecText = TimeUnit.MILLISECONDS.toSeconds(remainingMills)
            val seconds = diffInSecText % 60
            diffInSecText /= 60
            val minutes = diffInSecText % 60
            diffInSecText /= 60
            val hours = diffInSecText % 24
            diffInSecText /= 24
            val days = diffInSecText

            if (item.customColoredWallpaper == TRUE || item.imageBg == TRUE) {

                itemView.recycler_view_main_expanded_bg_event_days_value.text = days.toString()
                itemView.recycler_view_main_expanded_bg_event_hours_value.text = hours.toString()
                itemView.recycler_view_main_expanded_bg_event_minutes_value.text = minutes.toString()
                itemView.recycler_view_main_expanded_bg_event_seconds_value.text = seconds.toString()

            } else {
                itemView.recycler_view_main_expanded_event_days_value.text = days.toString()
                itemView.recycler_view_main_expanded_event_hours_value.text = hours.toString()
                itemView.recycler_view_main_expanded_event_minutes_value.text = minutes.toString()
                itemView.recycler_view_main_expanded_event_seconds_value.text = seconds.toString()
            }



            object : CountDownTimer(remainingMills, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val calendar = Calendar.getInstance()
                    calendar.timeZone = TimeZone.getDefault()
                    calendar.timeInMillis = millisUntilFinished

                    var diffInSec = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                    val seconds = diffInSec % 60
                    diffInSec /= 60
                    val minutes = diffInSec % 60
                    diffInSec /= 60
                    val hours = diffInSec % 24
                    diffInSec /= 24
                    val days = diffInSec

                    if (item.customColoredWallpaper == TRUE || item.imageBg == TRUE) {

                        itemView.recycler_view_main_expanded_bg_event_days_value.text = days.toString()
                        itemView.recycler_view_main_expanded_bg_event_hours_value.text = hours.toString()
                        itemView.recycler_view_main_expanded_bg_event_minutes_value.text = minutes.toString()
                        itemView.recycler_view_main_expanded_bg_event_seconds_value.text = seconds.toString()

                    } else {
                        itemView.recycler_view_main_expanded_event_days_value.text = days.toString()
                        itemView.recycler_view_main_expanded_event_hours_value.text = hours.toString()
                        itemView.recycler_view_main_expanded_event_minutes_value.text = minutes.toString()
                        itemView.recycler_view_main_expanded_event_seconds_value.text = seconds.toString()
                    }

                }

                override fun onFinish() {
                    DBHelper.database.countdownDao().updateFinished(Costants.COUNTDOWN_FINISHED, item.id)
                }
            }.start()
        }

        /*   fun setProgressBarVisibility(holder: BaseViewHolder, countdown: Countdown, context: Context) {

            if (countdown.showProgressBar == FALSE) {
                holder.setGone(R.id.progressBar, false)
                holder.getView<TextView>(R.id.recycler_view_main_event_name)
                val imageView = holder.getView<ImageView>(R.id.main_recycler_view_card_normal_bg_image)

                val dip = 65f
                val r = context.resources
                val px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dip,
                    r.displayMetrics
                )
                imageView.layoutParams.height = px.toInt()

            } else {
                holder.setGone(R.id.progressBar, true)
            }
        }
*/
        fun setTextColor(itemView: View, item: Countdown) {

            val color = Color.parseColor(item.color)

            if (item.expand == TRUE && item.imageBg == FALSE && item.customColoredWallpaper == FALSE && item.finished != TRUE) {
                itemView.recycler_view_main_expanded_event_name.setTextColor(color)
                itemView.recycler_view_main_expanded_event_date.setTextColor(color)
                itemView.recycler_view_main_expanded_event_days_value.setTextColor(color)
                itemView.recycler_view_main_expanded_event_days_type.setTextColor(color)
                itemView.recycler_view_main_expanded_event_hours_value.setTextColor(color)
                itemView.recycler_view_main_expanded_event_hours_type.setTextColor(color)
                itemView.recycler_view_main_expanded_event_minutes_value.setTextColor(color)
                itemView.recycler_view_main_expanded_event_minutes_type.setTextColor(color)
                itemView.recycler_view_main_expanded_event_seconds_value.setTextColor(color)
                itemView.recycler_view_main_expanded_event_seconds_type.setTextColor(color)
            } else if (item.expand == FALSE && item.imageBg == FALSE && item.customColoredWallpaper == FALSE && item.finished != TRUE) {
                itemView.recycler_view_main_event_name.setTextColor(color)
                itemView.recycler_view_main_event_date.setTextColor(color)
                itemView.recycler_view_main_event_remaining.setTextColor(color)
                itemView.recycler_view_main_event_remaining_type.setTextColor(color)
                itemView.progressBarItemNormal.setBackgroundColor(color)
            } else if (item.expand == TRUE && item.imageBg == TRUE && item.finished != TRUE || item.expand == TRUE && item.customColoredWallpaper == TRUE && item.finished != TRUE) {
                //EXPANDED BG IMAGE
                itemView.recycler_view_main_expanded_bg_event_name.setTextColor(color)
                itemView.recycler_view_main_expanded_bg_event_date.setTextColor(color)
                itemView.recycler_view_main_expanded_bg_event_days_value.setTextColor(color)
                itemView.recycler_view_main_expanded_bg_event_days_type.setTextColor(color)
                itemView.recycler_view_main_expanded_bg_event_hours_value.setTextColor(color)
                itemView.recycler_view_main_expanded_bg_event_hours_type.setTextColor(color)
                itemView.recycler_view_main_expanded_bg_event_minutes_value.setTextColor(color)
                itemView.recycler_view_main_expanded_bg_event_minutes_type.setTextColor(color)
                itemView.recycler_view_main_expanded_bg_event_seconds_value.setTextColor(color)
                itemView.recycler_view_main_expanded_bg_event_seconds_type.setTextColor(color)

            } else if (item.finished != TRUE) {
                itemView.recycler_view_main_bg_event_name.setTextColor(color)
                itemView.recycler_view_main_bg_event_date.setTextColor(color)
                itemView.progressBarNormalBg.setBackgroundColor(color)
                itemView.recycler_view_main_bg_event_remaining.setTextColor(color)
                itemView.recycler_view_mainbg__event_remaining_type.setTextColor(color)
            }

        }


        fun getFormattedDate(date: Date): String {
            val format =
                android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEE d MMM yyy")
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            return dateFormat.format(date)
        }


        fun getFormattedDateForEditNew(date: Date): String {
            val format =
                android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEEE d MMM yyy HH:mm")
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            return dateFormat.format(date)
        }

        /*

        fun setImageBg(holder: BaseViewHolder, countdown: Countdown, context: Context) {

            val uri = Uri.parse(countdown.imagePath)

            if (countdown.expand == TRUE) {
                val imageView = holder.getView<ImageView>(R.id.main_recycler_view_card_extended_bg_image)
                glideSetImage(imageView, uri, context)
            } else if (countdown.expand == FALSE) {
                val imageView = holder.getView<ImageView>(R.id.main_recycler_view_card_normal_bg_image)
                glideSetImage(imageView, uri, context)
            } else {
                val imageView = holder.getView<ImageView>(R.id.main_recycler_view_card_finish_bg_image)
                glideSetImage(imageView, uri, context)
            }
        }

        private fun glideSetImage(imageView: ImageView, uri: Uri, context: Context) {
            Glide.with(context)
                .load(uri)
                .thumbnail(0.1f)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(15, 2)))
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageView)
            Log.i("imageview", imageView.toString())
        }

*/
        /*
        fun setTextColor(holder: BaseViewHolder, countdown: Countdown) {
            if (countdown.expand == Costants.TRUE && countdown.finished != Costants.COUNTDOWN_FINISHED) {
                //  name
                holder.setTextColor(R.id.recycler_view_main_expanded_event_name, Color.parseColor(countdown.color))
                // date
                holder.setTextColor(R.id.recycler_view_main_expanded_event_date, Color.parseColor(countdown.color))

                holder.setTextColor(
                    R.id.recycler_view_main_expanded_event_seconds_value,
                    Color.parseColor(countdown.color)
                )
                holder.setTextColor(
                    R.id.recycler_view_main_expanded_event_minutes_value,
                    Color.parseColor(countdown.color)
                )

                holder.setTextColor(
                    R.id.recycler_view_main_expanded_event_hours_value,
                    Color.parseColor(countdown.color)
                )

                holder.setTextColor(
                    R.id.recycler_view_main_expanded_event_days_value,
                    Color.parseColor(countdown.color)
                )

                holder.setTextColor(R.id.recycler_view_main_expanded_event_days_type, Color.parseColor(countdown.color))
                holder.setTextColor(
                    R.id.recycler_view_main_expanded_event_hours_type,
                    Color.parseColor(countdown.color)
                )
                holder.setTextColor(
                    R.id.recycler_view_main_expanded_event_minutes_type,
                    Color.parseColor(countdown.color)
                )
                holder.setTextColor(
                    R.id.recycler_view_main_expanded_event_seconds_type,
                    Color.parseColor(countdown.color)
                )


            } else if (countdown.expand == Costants.FALSE && countdown.finished != Costants.COUNTDOWN_FINISHED) {
                holder.setTextColor(R.id.recycler_view_main_event_name, Color.parseColor(countdown.color))
                holder.setTextColor(R.id.recycler_view_main_event_date, Color.parseColor(countdown.color))
                holder.setTextColor(R.id.recycler_view_main_event_remaining, Color.parseColor(countdown.color))
                holder.setTextColor(R.id.recycler_view_main_event_remaining_type, Color.parseColor(countdown.color))
            } else {
                holder.setTextColor(R.id.recycler_view_main_finished_event_name, Color.parseColor(countdown.color))
                holder.setTextColor(R.id.recycler_view_finished_main_event_date, Color.parseColor(countdown.color))
                //val imageView =
                holder.getView<ImageView>(R.id.recycler_view_main_finished_icon)
                    .setColorFilter(Color.parseColor(countdown.color))
            }


        }
        */
/*
        fun setShadow(holder: BaseViewHolder, countdown: Countdown) {
            val shadowColor = Color.parseColor(Costants.SHADOW_COLOR)

            if (countdown.expand == 0 && countdown.finished != 1) {
                Log.i("set-shadow", countdown.name + "normal")
                holder.getView<TextView>(R.id.recycler_view_main_event_name).setShadowLayer(2f, 1f, 2f, shadowColor)
                holder.getView<TextView>(R.id.recycler_view_main_event_date).setShadowLayer(4f, 4f, 3f, shadowColor)
                holder.getView<TextView>(R.id.recycler_view_main_event_remaining)
                    .setShadowLayer(4f, 4f, 4f, shadowColor)
                holder.getView<TextView>(R.id.recycler_view_main_event_remaining_type)
                    .setShadowLayer(3f, 3f, 3f, shadowColor)
            } else if (countdown.expand == 1 && countdown.finished != 1) {
                Log.i("set-shadow", countdown.name + "expaned")
                holder.getView<TextView>(R.id.recycler_view_main_expanded_event_name)
                    .setShadowLayer(2f, 1f, 2f, shadowColor)
                holder.getView<TextView>(R.id.recycler_view_main_expanded_event_date)
                    .setShadowLayer(2f, 1f, 2f, shadowColor)
                holder.getView<TextView>(R.id.recycler_view_main_expanded_event_days_value)
                    .setShadowLayer(2f, 1f, 2f, shadowColor)
                holder.getView<TextView>(R.id.recycler_view_main_expanded_event_days_type)
                    .setShadowLayer(2f, 1f, 2f, shadowColor)
                holder.getView<TextView>(R.id.recycler_view_main_expanded_event_hours_value)
                    .setShadowLayer(2f, 1f, 2f, shadowColor)
                holder.getView<TextView>(R.id.recycler_view_main_expanded_event_hours_type)
                    .setShadowLayer(2f, 1f, 2f, shadowColor)
                holder.getView<TextView>(R.id.recycler_view_main_expanded_event_minutes_value)
                    .setShadowLayer(2f, 1f, 2f, shadowColor)
                holder.getView<TextView>(R.id.recycler_view_main_expanded_event_minutes_type)
                    .setShadowLayer(2f, 1f, 2f, shadowColor)
                holder.getView<TextView>(R.id.recycler_view_main_expanded_event_seconds_value)
                    .setShadowLayer(2f, 1f, 2f, shadowColor)
                holder.getView<TextView>(R.id.recycler_view_main_expanded_event_seconds_type)
                    .setShadowLayer(2f, 1f, 2f, shadowColor)


            } else {
                Log.i("set-shadow", countdown.name + "finish")
                holder.getView<TextView>(R.id.recycler_view_main_finished_event_name)
                    .setShadowLayer(2f, 1f, 2f, shadowColor)
                holder.getView<TextView>(R.id.recycler_view_finished_main_event_date)
                    .setShadowLayer(2f, 1f, 2f, shadowColor)
            }

        }

        fun setBackgroundColor(holder: BaseViewHolder, countdown: Countdown) {
            if (countdown.expand == Costants.FALSE) {
                Log.i("set-bg", countdown.name + " normal ")

                holder.setBackgroundColor(
                    R.id.main_recycler_view_card_normal_bg_image,
                    Color.parseColor(countdown.wallpaperColor)
                )
            } else if (countdown.expand == Costants.TRUE) {
                Log.i("set-bg", countdown.name + " expand ")

                holder.setBackgroundColor(
                    R.id.main_recycler_view_card_extended_bg_image,
                    Color.parseColor(countdown.wallpaperColor)
                )

            } else {
                Log.i("set-bg", countdown.name + " finish ")

                holder.setBackgroundColor(
                    R.id.main_recycler_view_card_finish_bg_image,
                    Color.parseColor(countdown.wallpaperColor)
                )
            }


        }
    }
    */
    }

}