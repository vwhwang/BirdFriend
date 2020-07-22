package com.example.birdfriend


import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters


class AddPostWorker(val appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {


        try {

            addNewPostCard()
            return Result.success()
            Log.i("testing", "AddPostWorker initiated!")


        } catch(e:Exception) {
            return Result.failure()
            Log.i("testing", "AddPostWorker initiated!")
        }
    }

    //set up func

    private  fun addNewPostCard(){
        //check if home or not
        val dbLog = LogStateDatabase.getDatabase(applicationContext)
        val homeStatus = dbLog.logStateDao().getLastState()[0].stateHomeAway

        val dbCard = UserCardsRoomDatabase.getDatabase(applicationContext)
        val cardInAlbum = dbCard.userCardsDao().getShowCards()

        val postCardsList = listOf("post_movie","post_moon"
            ,"lao_post","post_boat","chocolate_post","cow_post","ski_post","tower_1")
        //check all existing post names in db

        var existingCardName = mutableListOf<String>()

        for(card in cardInAlbum){
            existingCardName.add(card.imgname)
        }

        val cardToAdd = postCardsList - existingCardName

        if (homeStatus == "Away" && cardToAdd.isEmpty() == false ){

            var nameToAdd = cardToAdd.shuffled()[0]

            dbCard.userCardsDao().insertCards(UserCards(nameToAdd, false ))

            Log.i("testing","$nameToAdd was added!")
            //Send notification
            NotifyNewCard(appContext).sendAddPostNotification()

        }else{
            Log.i("testing","nothing was added!")

        }


    }


}





