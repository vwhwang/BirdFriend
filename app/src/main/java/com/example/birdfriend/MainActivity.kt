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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import com.example.birdfriend.HomeAwayWorker
import kotlinx.android.synthetic.main.fragment_second.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

//TRYING TO ADD NOTIFICATION HERE
lateinit var notificationManager: NotificationManager
lateinit var notificationChannel: NotificationChannel
lateinit var builder: Notification.Builder
private  val channelId = "com.example.birdfriend"
private  val description = "BirdFriend Notification"

class MainActivity : AppCompatActivity() {

    companion object{
        var mainHomeStatus = "TBD"
        var sendNewCard = true
    }
    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

//SET UP NOTIFICATION HERE IT WORKS!!!

//        if (sendNewCard == true) {
//            sendNewNotification()
//        }

        //HERE WILL LOG DATA EVERYTIME APP ON CREATE//
        // OnCreate will fire home or away
        setOneTimeWorkRequet()
        Log.i("MainActivity", "setOneTimeWorkRequet was called")


//ORIGINAL
//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }



        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {

            //COMMENT OUT ONE TIME THIS BELOW WORKS
//            setOneTimeWorkRequet()
            Log.i("MainActivity", "setOneTimeWorkRequet was called")

            // below show how to log data to log_state_table

            val db = LogStateDatabase.getDatabase(applicationContext)
            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = time.format(Date())
            //Logging last record
            Log.d("log_data", db.logStateDao().getLastState()[0].creationDate.toString())

        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun sendNewNotification() {
        notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, LauncherActivityInfo::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
            NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel.enableLights(true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel.enableVibration(false)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = Notification.Builder(this, channelId)
            .setContentTitle("Bird Notification:")
            .setContentText("You got mail from your BirdFriend!")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.bird_1)
        }

        notificationManager.notify(0, builder.build())
        sendNewCard = false
    }




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





    //SET ONE TIME (THIS WORKS GREAT)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setOneTimeWorkRequet(){
        val workManager =  WorkManager.getInstance(applicationContext)

        val backendLoad =  OneTimeWorkRequest.Builder(HomeAwayWorker::class.java)
//            .setInitialDelay(3, TimeUnit.MINUTES)
            .build()
        workManager.enqueue(backendLoad)
        workManager.getWorkInfoByIdLiveData(backendLoad.id)
            .observe(this, Observer {
//                textview_second.text = it.state.name
                Log.d("log_data",it.state.name)

                if(it.state.isFinished){
                    val data = it.outputData
                    val db = LogStateDatabase.getDatabase(applicationContext)
                    val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                    val currentDate = time.format(Date())
                    val newState = data.getString(HomeAwayWorker.KEY_STATUS).toString()
                    db.logStateDao().insertState(LogState(creationDate = currentDate, stateHomeAway = newState))

                    Log.d("log_data","THIS IS WITHIN WORK REQUEST")
                    Log.d("log_data",db.logStateDao().getLastState()[0].uid.toString())
                    Log.d("log_data",db.logStateDao().getLastState()[0].creationDate.toString())
                    Log.d("log_data",db.logStateDao().getLastState()[0].stateHomeAway.toString())

                }
            })

    }
}



















//NOTES//


// PEREODIC WORK EXAMPLE I

/**
private fun setPerioticStateRequest(){
val workManager =  WorkManager.getInstance(applicationContext)

val backendLoad =  PeriodicWorkRequest.Builder(
HomeAwayWorker::class.java, 16, TimeUnit.MINUTES)
.build()

workManager.enqueue(backendLoad)
workManager.getWorkInfoByIdLiveData(backendLoad.id)
.observe(this, Observer {
textview_second.text = it.state.name

Log.d("log_data",it.state.name)
Log.d("log_data",it.state.isFinished.toString())
//                val data = it.outputData

// LOG HOME STATUS BASED ON LOG STATE DB
val db = LogStateDatabase.getDatabase(applicationContext)
Log.d("log_data",db.logStateDao().getLastState()[0].uid.toString())
Log.d("log_data",db.logStateDao().getLastState()[0].creationDate.toString())
Log.d("log_data",db.logStateDao().getLastState()[0].stateHomeAway.toString())

})

}
 */






// ONE TIME WORK EXAMPLE II
/**
private fun setOneTimeWorkRequet(){
val workManager =  WorkManager.getInstance(applicationContext)

val backendLoad =  OneTimeWorkRequest.Builder(HomeAwayWorker::class.java)
.build()
workManager.enqueue(backendLoad)
workManager.getWorkInfoByIdLiveData(backendLoad.id)
.observe(this, Observer {
textview_second.text = it.state.name
if(it.state.isFinished){
val data = it.outputData


// LOG HOME STATUS BASED ON LOG STATE DB
val db = LogStateDatabase.getDatabase(applicationContext)
val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
val currentDate = time.format(Date())
val newState = data.getString(HomeAwayWorker.KEY_STATUS).toString()
db.logStateDao().insertState(LogState(creationDate = currentDate, stateHomeAway = newState))


/**
val mypreference = MyPreference(context = this)
mypreference.setHomeAway(data.getString(HomeAwayWorker.KEY_STATUS).toString())
mainHomeStatus = mypreference.getHomeAway().toString()
*/

// TOAST MESSAGE to show home or away
val message1 = data.getString(HomeAwayWorker.KEY_WORKER)
Toast.makeText(applicationContext,message1,Toast.LENGTH_LONG).show()
val messageStatus = data.getString(HomeAwayWorker.KEY_STATUS)
Toast.makeText(applicationContext,messageStatus,Toast.LENGTH_LONG).show()
}
})

}
        */




/** THIS IS PERIOTIC EXAMPLE
private fun setPerioticStateRequest(){
    val workManager =  WorkManager.getInstance(applicationContext)

    val backendLoad =  PeriodicWorkRequest.Builder(
        HomeAwayWorker::class.java, 15, TimeUnit.MINUTES)
        .build()

    workManager.enqueue(backendLoad)
    workManager.getWorkInfoByIdLiveData(backendLoad.id)
        .observe(this, Observer {


            //added attemp

            if (it != null){
                val data = it.outputData
                val db = LogStateDatabase.getDatabase(applicationContext)
                val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                val currentDate = time.format(Date())
                val newState = data.getString(HomeAwayWorker.KEY_STATUS).toString()
                db.logStateDao().insertState(LogState(creationDate = currentDate, stateHomeAway = newState))

            }
            textview_second.text = it.state.name

            Log.d("log_data",it.state.name)
            Log.d("log_data",it.state.isFinished.toString())

// LOG HOME STATUS BASED ON LOG STATE DB
            val db = LogStateDatabase.getDatabase(applicationContext)
            Log.d("log_data",db.logStateDao().getLastState()[0].uid.toString())
            Log.d("log_data",db.logStateDao().getLastState()[0].creationDate.toString())
            Log.d("log_data",db.logStateDao().getLastState()[0].stateHomeAway.toString())

        })

}
        */