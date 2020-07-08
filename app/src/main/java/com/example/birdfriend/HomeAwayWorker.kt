package com.example.birdfriend

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters


class HomeAwayWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    override fun doWork(): Result {

        // Do the work here--in this case, change show text display
        try {
            for (i in 1..6000) {
                Log.i("testing", "this is working $i")
            }

            return Result.success()
        } catch(e:Exception) {
                return Result.failure()
            }
        }

    }
