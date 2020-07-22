package com.example.birdfriend


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LogState::class], version = 1, exportSchema = false)


abstract class LogStateDatabase : RoomDatabase() {
    abstract fun logStateDao(): LogStateDao

    companion object {
        @Volatile
        private var INSTANCE: LogStateDatabase? = null

        fun getDatabase(context: Context): LogStateDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            val instance = Room.databaseBuilder(
                context.applicationContext,
                LogStateDatabase::class.java,
                "log_state_database"
            ).allowMainThreadQueries().build()
            INSTANCE = instance
            return instance
        }
    }
}
