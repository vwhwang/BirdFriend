package com.example.birdfriend

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "log_state_table")


class LogState (
    @PrimaryKey(autoGenerate = true) val uid: Long = 0L,
    @ColumnInfo(name = "creation_date") var creationDate: String,
    @ColumnInfo(name = "state_home_away") var stateHomeAway: String
)



