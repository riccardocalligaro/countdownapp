package com.riccardocalligaro.countdownapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.riccardocalligaro.countdownapp.fragments.HistoryFragment
import com.riccardocalligaro.countdownapp.fragments.MainFragment
import com.riccardocalligaro.countdownapp.fragments.SettingsFragment

class SectionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MainFragment()
            1 -> HistoryFragment()
            2 -> SettingsFragment()
            else -> MainFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }


}