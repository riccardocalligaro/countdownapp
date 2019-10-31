package com.riccardocalligaro.countdownapp.views

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.riccardocalligaro.countdownapp.db.DBHelper
import com.riccardocalligaro.countdownapp.db.entities.Countdown

class CountdownViewModel(application: Application) : AndroidViewModel(application) {

    internal val getAllCountdowns: LiveData<List<Countdown>> = DBHelper.database.countdownDao().getAllCountdowns()

    fun insert(countdown: Countdown) {
        DBHelper.database.countdownDao().insert(countdown)
    }
}