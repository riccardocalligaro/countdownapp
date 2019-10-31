package com.riccardocalligaro.countdownapp.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.util.*

@Entity(tableName = "COUNTDOWN_ITEMS")
data class Countdown(
    @ColumnInfo(name = "ID") @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "CREATED_DATE") @Expose val createdDate: Date = Date(0),
    @ColumnInfo(name = "NAME") @Expose val name: String = "",
    @ColumnInfo(name = "DATE") @Expose val date: Date = Date(0),
    @ColumnInfo(name = "STICK_TOP") @Expose val preferred: Int = 0, // if true stick on top
    @ColumnInfo(name = "DELETED") @Expose val deleted: Int = 0,
    @ColumnInfo(name = "EXPAND") @Expose val expand: Int = 0,
    @ColumnInfo(name = "FINISHED") @Expose val finished: Int = 0,
    @ColumnInfo(name = "BLURRY") @Expose val blurry: Int = 0,
    @ColumnInfo(name = "BLURRY_LEVEL") @Expose val blurryLevel: Int = 0,
    @ColumnInfo(name = "IMAGE_BG") @Expose val imageBg: Int = 0,
    @ColumnInfo(name = "IMAGE_PATH") @Expose val imagePath: String = "",
    @ColumnInfo(name = "TEXT_COLOR") @Expose val color: String = "#FFFFFF",
    @ColumnInfo(name = "SHOW_PROGRESS_BAR") @Expose val showProgressBar: Int = 0,
    @ColumnInfo(name = "CUSTOM_COLORED_WALLPAPER") @Expose val customColoredWallpaper: Int = 0,
    @ColumnInfo(name = "WALLPAPER_COLOR") @Expose val wallpaperColor: String = "#FFFFFF"
    )