package com.remcode.coloursforyou.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.remcode.coloursforyou.data.models.Colour

@Database(entities = [Colour::class],
    version = 1,
    exportSchema = true)
abstract class ColourDatabase : RoomDatabase() {

    abstract val colourDatabaseDao: ColourDatabaseDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ColourDatabase? = null

        fun getDatabase(context: Context): ColourDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ColourDatabase::class.java,
                    "colour_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}