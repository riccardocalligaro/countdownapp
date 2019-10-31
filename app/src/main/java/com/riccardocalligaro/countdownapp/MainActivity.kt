package com.riccardocalligaro.countdownapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.riccardocalligaro.countdownapp.adapters.SectionPagerAdapter
import com.riccardocalligaro.countdownapp.fragments.MainFragment
import com.riccardocalligaro.countdownapp.fragments.SettingsFragment
import com.riccardocalligaro.countdownapp.views.CountdownViewModel
import kotlinx.android.synthetic.main.activity_main.*
import androidx.viewpager.widget.ViewPager
import com.riccardocalligaro.countdownapp.activities.NewCountdown
import com.riccardocalligaro.countdownapp.db.DBHelper
import com.riccardocalligaro.countdownapp.fragments.HistoryFragment
import com.riccardocalligaro.countdownapp.notifications.CountdownReceiver
import com.riccardocalligaro.countdownapp.utils.Costants.EXPLORE_ICON_POSITION
import com.riccardocalligaro.countdownapp.utils.Costants.MAIN_ICON_POSITION
import com.riccardocalligaro.countdownapp.utils.Costants.SETTINGS_ICON_POSITION


class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener,
    HistoryFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener {


    private lateinit var countdownViewModel: CountdownViewModel
    private var mSectionsPagerAdapter: SectionPagerAdapter? = null
    lateinit var alarmManager: AlarmManager
    lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  openInitialFragment()
        mSectionsPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        tab_layout.setupWithViewPager(container)

        setupIcons()
        context = this
        // setupNotifications()


        /*
        todo: notifications service
        todo: fix finished countdowns
        todo: add settings - data format, dark theme, send bug report, translate
        todo: share countdown
        todo: widget
         */


        fab.setOnClickListener { view ->
            val intent = Intent(this, NewCountdown::class.java)
            startActivity(intent)
        }

    }

    private fun setupNotifications() {
        for (countdown in DBHelper.database.countdownDao().getCountdowndsFotNotification()) {
            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            Log.i("notification", "creatednot")
            val intent = Intent(context, CountdownReceiver::class.java)


            val broadcast = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP, countdown.date.time, broadcast
            )
        }

    }

    private fun setupIcons() {

        for (i in 0 until 3) {
            var iconId = -1
            when (i) {
                0 -> iconId = R.drawable.ic_timer_selected_24dp
                1 -> iconId = R.drawable.ic_history_black_24dp
                2 -> iconId = R.drawable.ic_settings_grey_24dp
            }
            tab_layout.getTabAt(i)?.setIcon(iconId)


        }
        tab_layout.setSelectedTabIndicatorColor(Color.parseColor("#6101ED"))
        tab_layout.getTabAt(MAIN_ICON_POSITION)?.icon?.setColorFilter(
            Color.parseColor("#6101ED"),
            PorterDuff.Mode.SRC_IN
        )


        container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        tab_layout.getTabAt(MAIN_ICON_POSITION)?.icon?.setColorFilter(
                            Color.parseColor("#6101ED"),
                            PorterDuff.Mode.SRC_IN
                        )
                        tab_layout.getTabAt(EXPLORE_ICON_POSITION)?.setIcon(R.drawable.ic_history_black_24dp)
                        tab_layout.getTabAt(SETTINGS_ICON_POSITION)?.setIcon(R.drawable.ic_settings_grey_24dp)
                        fab.show()
                    }

                    1 -> {
                        tab_layout.getTabAt(MAIN_ICON_POSITION)?.setIcon(R.drawable.ic_timer_grey_24dp)
                        tab_layout.getTabAt(EXPLORE_ICON_POSITION)?.icon?.setColorFilter(
                            Color.parseColor("#6101ED"),
                            PorterDuff.Mode.SRC_IN
                        )
                        tab_layout.getTabAt(SETTINGS_ICON_POSITION)?.setIcon(R.drawable.ic_settings_grey_24dp)
                        fab.hide()
                    }

                    2 -> {
                        tab_layout.getTabAt(MAIN_ICON_POSITION)?.setIcon(R.drawable.ic_timer_grey_24dp)
                        tab_layout.getTabAt(EXPLORE_ICON_POSITION)?.setIcon(R.drawable.ic_history_black_24dp)
                        tab_layout.getTabAt(SETTINGS_ICON_POSITION)?.icon?.setColorFilter(
                            Color.parseColor("#6101ED"),
                            PorterDuff.Mode.SRC_IN
                        )
                        fab.hide()
                    }
                }
                // tab_layout.getTabAt(position)?.setIcon(R.drawable.ic_timer_grey_24dp)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })


        // tab_layout.setOnTabSelectedListener(object : )
    }


    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
