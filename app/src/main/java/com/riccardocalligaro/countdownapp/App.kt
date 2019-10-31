package com.riccardocalligaro.countdownapp

import android.app.Application
import com.riccardocalligaro.countdownapp.db.DBHelper

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        DBHelper.createDb(this)
    }


}