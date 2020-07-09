package com.example.birdfriend

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
import com.example.birdfriend.HomeAwayWorker
import kotlinx.android.synthetic.main.fragment_second.*

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

        val mypreference = MyPreference(context = this)
        mypreference.setHomeAway(mainHomeStatus.toString())
        mainHomeStatus = mypreference.getHomeAway().toString()


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            setOneTimeWorkRequet()
            Log.d("main", mainHomeStatus.toString())
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


                    // doesn't work if restart

                    mainHomeStatus = data.getString(HomeAwayWorker.KEY_STATUS).toString()

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
}