package com.example.birdfriend

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.LauncherActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.birdfriend.HomeAwayWorker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


lateinit var notificationManager: NotificationManager
lateinit var notificationChannel: NotificationChannel
lateinit var builder: Notification.Builder
private  val channelId = "com.example.birdfriend"
private  val description = "BirdFriend Notification"

class MainActivity : AppCompatActivity() {

    companion object{
        var mainHomeStatus = "TBD"
        var sendNewCard = true
        var setBird = (0..2).random()

    }

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))


        //hide toolbar
        getSupportActionBar()?.hide()

        Log.i("MainActivity", "onCreate was called.")


        val packbutton = findViewById<FloatingActionButton>(R.id.fab)


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            //set up message random messages to show
            val messagesToShow = listOf<String>("some apples are packed!", "some love is packed!",
                "a dagger is packed...", "some wine is packed!!!","a powerful lucky charm is packed.","a rain coat is packed!")
            var message = messagesToShow.shuffled()[0]
            //if Home show stuff packed if not show he's not home yet.

            val dbLogState = LogStateDatabase.getDatabase(applicationContext)
            val lastState = dbLogState.logStateDao().getLastState()

            if (lastState.isNotEmpty() && lastState.first().stateHomeAway == "Home") {
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                packbutton.isVisible = false
            } else if (lastState.isNotEmpty() && lastState.first().stateHomeAway == "Away"){
                Toast.makeText(applicationContext,"bird is not home yet!",Toast.LENGTH_LONG).show()
                packbutton.isVisible = false
            } else {
                Toast.makeText(applicationContext,"$1000 is packed for bird to use!",Toast.LENGTH_LONG).show()
                packbutton.isVisible = false
            }


            //Set up Home or Away
            setOneTimeWorkRequet()
            Log.i("MainActivity", "setOneTimeWorkRequet was called")


            //addpostwork request to send notification and add post card
            setOneTimeAddPostWorkRequest()
            Log.i("MainActivity", "setOneTimeAddPostWorkRequest was called")



        }
        //set up bird random positions
        setBird = (0..2).random()
        Log.i("MainActivity","onCreated Called $setBird")


        val dbLogState = LogStateDatabase.getDatabase(applicationContext)
        val lastState = dbLogState.logStateDao().getLastState()

        if (lastState.isNotEmpty() && lastState.first().stateHomeAway == "Home") {
            packbutton.isVisible = true
        } else {
            if (setBird == 1 ){
                packbutton.isVisible = true
            } else {
                packbutton.isVisible = false
            }
        }


    }


    override fun onRestart() {
        super.onRestart()
        setBird = (0..2).random()
        Log.i("MainActivity", "onRestart Called $setBird")

        //onRestart make it random to show pack or not always show for now
        val packbutton = findViewById<FloatingActionButton>(R.id.fab)
        val dbLogState = LogStateDatabase.getDatabase(applicationContext)
        val lastState = dbLogState.logStateDao().getLastState()

        if (lastState.isNotEmpty() && lastState.first().stateHomeAway == "Home") {
            packbutton.isVisible = true
        } else {
            if (setBird == 1) {
                packbutton.isVisible = true
            } else {
                packbutton.isVisible = false
            }
        }
    }



//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    public fun sendNewNotification() {
//        notificationManager =
//            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        val intent = Intent(this, LauncherActivityInfo::class.java)
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationChannel =
//            NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationChannel.enableLights(true)
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationChannel.enableVibration(false)
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            builder = Notification.Builder(this, channelId)
//            .setContentTitle("Bird Notification:")
//            .setContentText("You got mail from your BirdFriend!")
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .setSmallIcon(R.drawable.bird_1)
//        }
//
//        notificationManager.notify(0, builder.build())
//        sendNewCard = false
//    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }




    //ONE TIME WORK ASK FOR HOME OR AWAY

    private fun setOneTimeWorkRequet(){
        val workManager =  WorkManager.getInstance(applicationContext)

        val backendLoad =  OneTimeWorkRequest.Builder(HomeAwayWorker::class.java)
            .setInitialDelay(7, TimeUnit.SECONDS)
            .build()
        workManager.enqueue(backendLoad)
        workManager.beginUniqueWork("Unique", ExistingWorkPolicy.KEEP , OneTimeWorkRequest.from(HomeAwayWorker::class.java))
        workManager.getWorkInfoByIdLiveData(backendLoad.id)
            .observe(this, Observer {
                Log.d("log_data",it.state.name)

                if(it.state.isFinished){
                    val data = it.outputData
                    val db = LogStateDatabase.getDatabase(applicationContext)
                    val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                    val currentDate = time.format(Date())
                    val newState = data.getString(HomeAwayWorker.KEY_STATUS).toString()


                    Log.d("log_data","THIS IS WITHIN WORK REQUEST")
                    Log.d("log_data",db.logStateDao().getLastState()[0].uid.toString())
                    Log.d("log_data",db.logStateDao().getLastState()[0].creationDate.toString())
                    Log.d("log_data",db.logStateDao().getLastState()[0].stateHomeAway.toString())

                }
            })

    }

    //AddPostWorker Function
    private fun setOneTimeAddPostWorkRequest(){
        val workManager = WorkManager.getInstance(applicationContext)

        val addPostLoad = OneTimeWorkRequest.Builder(AddPostWorker::class.java)
            .setInitialDelay(30, TimeUnit.SECONDS)
            .build()
        workManager.enqueue(addPostLoad)
        workManager.beginUniqueWork("Unique",ExistingWorkPolicy.KEEP, OneTimeWorkRequest.from(AddPostWorker::class.java))
        workManager.getWorkInfoByIdLiveData(addPostLoad.id)
            .observe(this, Observer {
                Log.d("log_data",it.state.name)

                if(it.state.isFinished){
                    Log.d("log_data","THIS IS WITHIN Add Post WORK REQUEST")
                    Log.d("log_data","new post false was added to database!")
                }


            })
    }

}


