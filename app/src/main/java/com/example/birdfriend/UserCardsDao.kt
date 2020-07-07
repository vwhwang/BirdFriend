package com.example.birdfriend
import androidx.room.*

@Dao
interface UserCardsDao {
    @Query("SELECT * from user_cards_table ")
    fun getAlluserCards():List<UserCards>

//    @Query("SELECT * from user_cards_table WHERE card_status = True")
//    fun getShowCards(): List<UserCards>

    @Insert
    fun insertCards(card: UserCards)

    @Query("DELETE FROM user_cards_table")
    fun deleteAll()
}