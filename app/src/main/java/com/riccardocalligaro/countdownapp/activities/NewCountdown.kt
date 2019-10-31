package com.riccardocalligaro.countdownapp.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog

import com.afollestad.materialdialogs.datetime.dateTimePicker
import com.afollestad.materialdialogs.list.listItems

import com.riccardocalligaro.countdownapp.R
import com.riccardocalligaro.countdownapp.db.DBHelper
import com.riccardocalligaro.countdownapp.db.entities.Countdown
import com.riccardocalligaro.countdownapp.utils.Costants.REQUEST_SELECT_IMAGE_IN_ALBUM
import com.riccardocalligaro.countdownapp.utils.Costants.TEXT_COLOR
import com.riccardocalligaro.countdownapp.utils.Costants.WHITE
import com.riccardocalligaro.countdownapp.utils.Methods.Companion.getFormattedDateForEditNew
import com.thebluealliance.spectrum.SpectrumDialog
import kotlinx.android.synthetic.main.activity_new_countdown.*
import kotlinx.android.synthetic.main.activity_new_countdown.view.*
import org.jetbrains.anko.toast
import java.util.*


class NewCountdown : AppCompatActivity() {

    private var selectedDate: Calendar? = null
    private var selectedName: String? = null
    private var imagePath: Uri? = null
    private var textColor: String? = null
    private var backGroundColor: String? = null

    lateinit var alarmManager: AlarmManager
    lateinit var context: Context


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_countdown)

        setSupportActionBar(new_countdown_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        context = this

        new_countdown_date_button.setOnClickListener {
            MaterialDialog(this).show {
                dateTimePicker(requireFutureDateTime = true, show24HoursView = true) { dialog, datetime ->
                    selectedDate = datetime
                    if (selectedDate != null) {
                        it.new_countdown_date_button.text = getFormattedDateForEditNew(selectedDate!!.time)
                    }

                }

            }
        }


        new_countdown_image_button.setOnClickListener {
            MaterialDialog(this).show {
                listItems(R.array.image_options) { dialog, index, text ->
                    if (index == 0) {
                        selectImageInAlbum()
                    } else {
                        showWallpaperDialog()
                    }
                }
            }

        }


        new_countdown_text_button.setOnClickListener {
            showColorDialog()
        }

        new_countdown_submit_button.setOnClickListener {
            val shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake)


            selectedName = new_countdown_edit_input.text.toString()

            if (selectedName != "") {
                if (selectedDate != null) {
                    val date: Date? = selectedDate!!.time
                    if (textColor == null) {
                        textColor = if (imagePath != null) {
                            WHITE
                        } else {
                            TEXT_COLOR
                        }
                    }

                    if (imagePath != null && backGroundColor == null) {

                        DBHelper.database.countdownDao().insert(
                            Countdown(
                                0L,
                                Calendar.getInstance().time,
                                selectedName!!,
                                date!!,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                1,
                                imagePath!!.toString(),
                                textColor!!
                            )
                        )
                    } else if (backGroundColor != null) {
                        DBHelper.database.countdownDao()
                            .insert(
                                Countdown(
                                    0L,
                                    Calendar.getInstance().time,
                                    selectedName!!,
                                    date!!,
                                    0,
                                    0,
                                    0,
                                    0,
                                    0,
                                    0,
                                    0,
                                    "",
                                    textColor!!,
                                    0,
                                    1,
                                    backGroundColor!!
                                )
                            )

                    } else {
                        DBHelper.database.countdownDao()
                            .insert(
                                Countdown(
                                    0L,
                                    Calendar.getInstance().time,
                                    selectedName!!,
                                    date!!,
                                    0,
                                    0,
                                    0,
                                    0,
                                    0,
                                    0,
                                    0,
                                    "",
                                    textColor!!,
                                    0,
                                    0

                                    )
                            )
                    }

                    // setupNotification()
                    finish()
                } else {
                    new_countdown_date_button.startAnimation(shakeAnim)
                    toast(getString(R.string.must_select_date))
                }
            } else {
                new_countdown_edit_input.startAnimation(shakeAnim)
                toast(getString(R.string.must_select_name))
            }

        }


    }

    private fun showColorDialog() {

        var selectedColor: Int? = null

        selectedColor = if (imagePath != null) {
            Color.parseColor(WHITE)
        } else {
            Color.parseColor(TEXT_COLOR)
        }

        SpectrumDialog.Builder(context)
            .setColors(R.array.text_colors)
            .setSelectedColor(selectedColor)
            .setDismissOnColorSelected(false)
            .setOutlineWidth(2)
            .setOnColorSelectedListener(SpectrumDialog.OnColorSelectedListener { positiveResult, color ->
                if (positiveResult) {
                    textColor = "#" + Integer.toHexString(color).toUpperCase()
                }
            }).build().show(supportFragmentManager, "Color dialog")
    }

    private fun showWallpaperDialog() {

        SpectrumDialog.Builder(context)
            .setColors(R.array.text_colors)
            .setSelectedColor(Color.parseColor(WHITE))
            .setDismissOnColorSelected(false)
            .setOutlineWidth(2)
            .setOnColorSelectedListener(SpectrumDialog.OnColorSelectedListener { positiveResult, color ->
                if (positiveResult) {
                    backGroundColor = "#" + Integer.toHexString(color).toUpperCase()
                }
            }).build().show(supportFragmentManager, "Background color dialog")
    }

    private fun setupNotification() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(".notifications.CountdownReceiver")
        val broadcast = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis + 1000, broadcast
        )
    }

    private fun selectImageInAlbum() {
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //intent.type = "image/*"
        //intent.putExtra("id", 56)
        if (i.resolveActivity(packageManager) != null) {
            startActivityForResult(i, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                imagePath = data?.data
            } else if (resultCode == Activity.RESULT_CANCELED) {
                toast(getString(R.string.operation_cancelled))
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        //  toast("pressed")
        if (selectedDate != null || selectedName != null) {
            MaterialDialog(this).show {
                title(R.string.exit_without_saving_title)
                message(R.string.exit_without_saving_message)
                positiveButton(R.string.no) {
                    this.hide()
                }
                negativeButton(R.string.exit_without_saving) {
                    onBackPressed()
                    finish()
                }
            }
        } else {
            onBackPressed()
            return true
        }

        return false
    }
}
