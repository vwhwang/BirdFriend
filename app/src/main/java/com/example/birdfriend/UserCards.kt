package com.example.birdfriend

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "user_cards_table")
class UserCards (
    @PrimaryKey  @ColumnInfo(name = "uid") val uid: Int,
    @ColumnInfo(name = "img_name") val imgname: String,
    @ColumnInfo(name = "card_status") var cardstatus: Boolean = false
)