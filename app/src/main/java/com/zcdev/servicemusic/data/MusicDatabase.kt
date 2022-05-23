package com.zcdev.servicemusic.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zcdev.servicemusic.data.models.Music


@Database(entities = [Music::class], version = 1, exportSchema = false)

abstract class MusicDatabase : RoomDatabase() {

 //   abstract fun toDoDao(): MusicDao


    companion object {
        @Volatile
        private var INSTANCE: MusicDatabase? = null

        fun getDatabase(context: Context): MusicDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MusicDatabase::class.java, "music_database"
            ).build()
    }

}