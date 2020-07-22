package com.example.birdfriend

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*


class HomeAwayWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    companion object{
        const val KEY_WORKER = "key_worker"
        const val KEY_STATUS = "key_status"
    }
    override fun doWork(): Result {

        try {
            setHomeAwayStatus()
            return Result.success()
            Log.i("testing", "worker initiated!")

        } catch(e:Exception) {
                return Result.failure()
                Log.i("testing", "worker failed!")
            }
        }

    //set up func
    private  fun setHomeAwayStatus(){

        Log.i("testing", "worker initiated!")

        val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = time.format(Date())

        val option = arrayOf("Home", "Away")
        val roll =  option[(0..1).random()]

        val db = LogStateDatabase.getDatabase(applicationContext)
        db.logStateDao().insertState(LogState(creationDate = currentDate, stateHomeAway = roll))
        }

    }

