package com.riccardocalligaro.countdownapp.db

import android.content.Context
import androidx.room.Room
import java.util.concurrent.atomic.AtomicBoolean

object DBHelper {

    lateinit var database: RoomDB

    private val initialized = AtomicBoolean(false)

    fun createDb(context: Context) {
        if (initialized.compareAndSet(false, true)) {
            database = Room.databaseBuilder(context, RoomDB::class.java, "countdown.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }


}