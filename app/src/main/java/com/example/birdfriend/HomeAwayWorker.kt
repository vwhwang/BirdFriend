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

            Log.i("testing", "worker initiated!")

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



//OUTSIDE DATA EXAMPLE
/**
try {

Log.i("testing", "worker initiated!")

val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
val currentDate = time.format(Date())

val option = arrayOf("Home", "Away")
val roll =  option[(0..1).random()]


val outPutData = Data.Builder()
.putString(KEY_WORKER, currentDate )
.putString(KEY_STATUS,roll)
.build()

//TESTING2
val db = LogStateDatabase.getDatabase(applicationContext)
val newState = outPutData.getString(HomeAwayWorker.KEY_STATUS).toString()
val dateInsert = outPutData.getString(HomeAwayWorker.KEY_WORKER).toString()
db.logStateDao().insertState(LogState(creationDate = dateInsert, stateHomeAway = newState))


//TESTING
/*
val db = LogStateDatabase.getDatabase(applicationContext)
//            val newState = outPutData.getString(HomeAwayWorker.KEY_STATUS).toString()
db.logStateDao().insertState(LogState(creationDate = currentDate, stateHomeAway = roll))
*/

return Result.success(outPutData)
} catch(e:Exception) {
return Result.failure()
}
}

        */

//PASSING Logging data directly here which cause multiple logs in DB (seems dangerous)

/**
class HomeAwayWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    companion object{
        const val KEY_WORKER = "key_worker"
        const val KEY_STATUS = "key_status"
    }
    override fun doWork(): Result {

        // Do the work here--in this case, change show text display
        try {

            Log.i("testing", "worker initiated!")

            //USING OUTSIDE METHOD
            setHomeAwayStatus()

            return Result.success()
        } catch(e:Exception) {
            return Result.failure()
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

*/
