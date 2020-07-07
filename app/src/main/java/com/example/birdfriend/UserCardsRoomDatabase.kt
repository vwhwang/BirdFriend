package com.example.birdfriend

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [UserCards::class], version = 1, exportSchema = false)
abstract class UserCardsRoomDatabase : RoomDatabase() {
    abstract fun userCardsDao(): UserCardsDao

    companion object {
        @Volatile
        private var INSTANCE: UserCardsRoomDatabase? = null

        fun getDatabase(context: Context): UserCardsRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            val instance = Room.databaseBuilder(
                context.applicationContext,
                UserCardsRoomDatabase::class.java,
                "user_cards_database"
            ).allowMainThreadQueries().build()
            INSTANCE = instance
            return instance
        }
    }
}
