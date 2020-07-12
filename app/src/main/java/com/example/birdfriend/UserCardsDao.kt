package com.example.birdfriend
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserCardsDao {
    @Query("SELECT * from user_cards_table ")
    fun getAlluserCards():List<UserCards>

    @Query("SELECT * from user_cards_table WHERE card_status = 1")
    fun getShowCards(): List<UserCards>

    @Query("SELECT * from user_cards_table WHERE card_status = 0")
    fun getNewCards(): List<UserCards>

    //UPDATE STATUS
    @Query("UPDATE user_cards_table SET card_status = :to_status WHERE img_name = :img_to_change")
    fun updateCard(img_to_change: String, to_status: Boolean): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCards(card: UserCards)


    @Query("DELETE FROM user_cards_table")
    fun deleteAll()
}