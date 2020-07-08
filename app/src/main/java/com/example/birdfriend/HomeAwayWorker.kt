package com.example.birdfriend

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters


class HomeAwayWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    public var TEXTING_TEXT : String = "home"
    override fun doWork(): Result {

        // Do the work here--in this case, change show text display


        if (TEXTING_TEXT == "home"){
            TEXTING_TEXT = "away"
        } else {
            TEXTING_TEXT = "home"
        }


        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }
}