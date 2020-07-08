package com.example.birdfriend

import android.content.Context
import android.util.Log
import androidx.work.Data
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

        // Do the work here--in this case, change show text display
        try {

            for (i in 1..6000) {
                Log.i("testing", "this is working $i")
            }

            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = time.format(Date())

            val option = arrayOf("Home", "Away")
            val roll =  option[(0..1).random()]


            val outPutData = Data.Builder()
                .putString(KEY_WORKER, currentDate )
                .putString(KEY_STATUS,roll)
                .build()

            return Result.success(outPutData)
        } catch(e:Exception) {
                return Result.failure()
            }
        }

    }
