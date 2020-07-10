package com.example.birdfriend

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
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

class MainActivity : AppCompatActivity() {

    companion object{
        var mainHomeStatus = "TBD"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        //set up my preference
/**
        val mypreference = MyPreference(context = this)
        mypreference.setHomeAway(mainHomeStatus.toString())
        mainHomeStatus = mypreference.getHomeAway().toString()
*/

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {

            //COMMENT OUT ONE TIME THIS BELOW WORKS
            setOneTimeWorkRequet()

            // below try Periodic Work request function currently works but log multiplie times
//            setPerioticStateRequest()



            // below show how to log data to log_state_table

            val db = LogStateDatabase.getDatabase(applicationContext)
            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = time.format(Date())
            // add data
//            db.logStateDao().insertState(LogState(creationDate = currentDate, stateHomeAway = "Home"))
            Log.d("log_data", db.logStateDao().getLastState()[0].creationDate.toString())

        }

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

    //
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

    //SET ONE TIME (THIS WORKS GREAT)
    private fun setOneTimeWorkRequet(){
        val workManager =  WorkManager.getInstance(applicationContext)

        val backendLoad =  OneTimeWorkRequest.Builder(HomeAwayWorker::class.java)
            .setInitialDelay(1, TimeUnit.MINUTES)
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



//                    val db = LogStateDatabase.getDatabase(applicationContext)
                    Log.d("log_data",db.logStateDao().getLastState()[0].uid.toString())
                    Log.d("log_data",db.logStateDao().getLastState()[0].creationDate.toString())
                    Log.d("log_data",db.logStateDao().getLastState()[0].stateHomeAway.toString())

                }
            })

    }
}







// PEREODIC WORK EXAMPLE

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






// ONE TIME WORK EXAMPLE
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

// LOG PEREODIC HERE VERSION II

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
val data = it.outputData


// LOG HOME STATUS BASED ON LOG STATE DB
val db = LogStateDatabase.getDatabase(applicationContext)
val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
val currentDate = time.format(Date())

//                val newState = data.getString(HomeAwayWorker.KEY_STATUS).toString()
//pass home away method here
val option = arrayOf("Home", "Away")
val roll =  option[(0..1).random()]
db.logStateDao().insertState(LogState(creationDate = currentDate, stateHomeAway = roll))

// LOG HOME STATUS BASED ON LOG STATE DB
Log.d("log_data",db.logStateDao().getLastState()[0].uid.toString())
Log.d("log_data",db.logStateDao().getLastState()[0].creationDate.toString())
Log.d("log_data",db.logStateDao().getLastState()[0].stateHomeAway.toString())

})

}
 */