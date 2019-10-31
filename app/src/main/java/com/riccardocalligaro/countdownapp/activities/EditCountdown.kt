package com.riccardocalligaro.countdownapp.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.dateTimePicker
import com.afollestad.materialdialogs.list.listItems
import com.riccardocalligaro.countdownapp.R
import com.riccardocalligaro.countdownapp.db.DBHelper
import com.riccardocalligaro.countdownapp.db.entities.Countdown
import com.riccardocalligaro.countdownapp.utils.Costants
import com.riccardocalligaro.countdownapp.utils.Costants.BACKGROUND_MODIFIED
import com.riccardocalligaro.countdownapp.utils.Costants.COUNTDOWN_ID
import com.riccardocalligaro.countdownapp.utils.Costants.IMAGE_MODIFIED
import com.riccardocalligaro.countdownapp.utils.Methods.Companion.getFormattedDate
import com.riccardocalligaro.countdownapp.utils.Methods.Companion.getFormattedDateForEditNew
import com.thebluealliance.spectrum.SpectrumDialog
import kotlinx.android.synthetic.main.activity_edit_countdown.*
import kotlinx.android.synthetic.main.activity_edit_countdown.view.*
import org.jetbrains.anko.toast
import java.util.*

class EditCountdown : AppCompatActivity() {

    private var id: Long = 0
    private var countdown: Countdown? = null
    private var imagePath: String? = null
    private var backgroundOrImageModified: Int? = null

    lateinit var context: Context

    private var backGroundColor: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_countdown)

        setSupportActionBar(edit_toolbar)

        context = this

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // set edit text to current item name

        id = intent.getLongExtra(COUNTDOWN_ID, 0)
        countdown = DBHelper.database.countdownDao().getCountdownById(id)

        var mDate: Date = countdown!!.date
        imagePath = countdown!!.imagePath
        backGroundColor = countdown!!.wallpaperColor

        edit_name_text?.setText(countdown!!.name)

        edit_date_button?.text = getFormattedDateForEditNew(countdown!!.date)

        edit_date_button.setOnClickListener {
            MaterialDialog(this).show {
                dateTimePicker(requireFutureDateTime = true, show24HoursView = true) { dialog, datetime ->
                    mDate = datetime.time
                    it.edit_date_button.text = getFormattedDateForEditNew(mDate)

                }
            }
        }

        edit_save_button.setOnClickListener {
            DBHelper.database.countdownDao()
                .updateCountdown(id, edit_name_text.text.toString(), mDate)
            if (backgroundOrImageModified!! == IMAGE_MODIFIED) {
                DBHelper.database.countdownDao().updateImagePath(imagePath!!, id)
                Log.i("img", backgroundOrImageModified!!.toString())
            } else if (backgroundOrImageModified!! == BACKGROUND_MODIFIED) {
                Log.i("img", backGroundColor!!)
                DBHelper.database.countdownDao().updateWallpaper(backGroundColor!!, id)
            }


            finish()
            //startActivity(Intent(activity, MainActivity::class.java))
        }

        edit_image_button.setOnClickListener {
            MaterialDialog(this).show {
                listItems(R.array.image_options) { dialog, index, text ->
                    if (index == 0) {
                        selectImageInAlbum()

                    } else {
                        showWallpaperDialog()

                    }
                }
            }
            //selectImageInAlbum()
        }


    }

    private fun showWallpaperDialog() {

        SpectrumDialog.Builder(context)
            .setColors(R.array.text_colors)
            .setSelectedColor(Color.parseColor(Costants.WHITE))
            .setDismissOnColorSelected(false)
            .setOutlineWidth(2)
            .setOnColorSelectedListener(SpectrumDialog.OnColorSelectedListener { positiveResult, color ->
                if (positiveResult) {
                    backGroundColor = "#" + Integer.toHexString(color).toUpperCase()
                    backgroundOrImageModified = BACKGROUND_MODIFIED

                }
            }).build().show(supportFragmentManager, "Background color dialog")
    }

    private fun selectImageInAlbum() {
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (i.resolveActivity(packageManager) != null) {
            startActivityForResult(i, Costants.REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_countdown, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_edit_countdown_delete -> {
                MaterialDialog(this).show {
                    title(R.string.delete_countdown_title)
                    // titleFont

                    positiveButton(R.string.delete) {
                        DBHelper.database.countdownDao().delete(countdown!!)
                        finish()
                    }
                    negativeButton(R.string.cancel) { dialog ->
                        dialog.hide()
                    }
                }

            }
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Costants.REQUEST_SELECT_IMAGE_IN_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                if (countdown != null) {
                    imagePath = data?.data.toString()
                    backgroundOrImageModified = IMAGE_MODIFIED
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                toast(getString(R.string.operation_cancelled))
            }

        }
    }


}
