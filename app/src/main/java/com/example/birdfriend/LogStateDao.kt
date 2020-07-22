package com.example.birdfriend

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LogStateDao {
    @Query("SELECT * from log_state_table ")
    fun getlogState():List<LogState>

    @Query("SELECT * from log_state_table ORDER BY uid DESC limit 1")
    fun getLastState(): List<LogState>

    @Insert
    fun insertState(logstate: LogState)

    @Query("DELETE FROM log_state_table")
    fun deleteAllState()
}