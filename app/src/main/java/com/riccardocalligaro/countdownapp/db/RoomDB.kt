package com.riccardocalligaro.countdownapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.riccardocalligaro.countdownapp.db.dao.CountdownDao
import com.riccardocalligaro.countdownapp.db.entities.Countdown

@Database(
    entities = [
        (Countdown::class)
    ],
    version = 9
)

@TypeConverters(Converters::class)

abstract class RoomDB : RoomDatabase() {
    // dao
    abstract fun countdownDao(): CountdownDao
}