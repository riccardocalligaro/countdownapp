package com.riccardocalligaro.countdownapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.riccardocalligaro.countdownapp.db.entities.Countdown
import java.util.*

@Dao
interface CountdownDao {

    @Query("SELECT * FROM COUNTDOWN_ITEMS ORDER BY DATE")
    fun getAllCountdowns(): LiveData<List<Countdown>>

    @Query("SELECT * FROM COUNTDOWN_ITEMS")
    fun getCountdowndsFotNotification(): List<Countdown>

    @Query("SELECT * FROM COUNTDOWN_ITEMS WHERE ID = :id")
    fun getCountdownById(id: Long): Countdown

    @Query("SELECT * FROM COUNTDOWN_ITEMS WHERE DATE >= :date")
    fun getNextCountdowns(date: Date): LiveData<List<Countdown>>

    @Query("SELECT * FROM COUNTDOWN_ITEMS WHERE DATE < :date")
    fun getPassedCountdowns(date: Date): LiveData<List<Countdown>>

    @Query("UPDATE COUNTDOWN_ITEMS SET NAME = :name, DATE =  :date WHERE ID = :id ")
    fun updateCountdown(id: Long, name: String, date: Date)


    @Query("DELETE FROM COUNTDOWN_ITEMS")
    fun deleteAll()

    @Query("UPDATE COUNTDOWN_ITEMS SET EXPAND = :newExpandValue WHERE ID =:id")
    fun updateExpand(newExpandValue: Int, id: Long)

    @Query("UPDATE COUNTDOWN_ITEMS SET FINISHED = :newFinishedValue WHERE ID =:id")
    fun updateFinished(newFinishedValue: Int, id: Long)

    @Query("UPDATE COUNTDOWN_ITEMS SET IMAGE_PATH = :imagePath, IMAGE_BG = 1, CUSTOM_COLORED_WALLPAPER = 0 WHERE ID =:id")
    fun updateImagePath(imagePath: String, id: Long)

    @Query("UPDATE COUNTDOWN_ITEMS SET CUSTOM_COLORED_WALLPAPER = 1, WALLPAPER_COLOR = :wallpaper_color, IMAGE_BG = 0 WHERE ID = :id ")
    fun updateWallpaper(wallpaper_color: String, id: Long)


    @Delete
    fun delete(countdown: Countdown)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(event: Countdown)


}