package com.example.birdfriend

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.NotificationChannel
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi

class NotifyNewCard : AppCompatActivity(){

        private var notificationManager: NotificationManager? = null

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
//            setContentView(R.layout.activity_notify_demo)

            notificationManager =
                getSystemService(
                    Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(
                    "com.example.birdfriend",
                    "NotifyDemo News",
                    "BirdFriend Notification")
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun createNotificationChannel(id: String, name: String,
                                              description: String) {

            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(id, name, importance)

            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager?.createNotificationChannel(channel)
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification(view: View) {

        val notificationID = 101

        val channelID = "com.example.birdfriend"

        val notification = Notification.Builder(this,
            channelID)
            .setContentTitle("Example Notification")
            .setContentText("This is an  example notification.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setChannelId(channelID)
            .build()

        com.example.birdfriend.notificationManager?.notify(notificationID, notification)
    }

}


// NOTIFICATION WITHIN CLICK
/*


        view.findViewById<Button>(R.id.temp_notify_button).setOnClickListener{

            notificationManager = this.activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val intent = Intent(activity, LauncherActivityInfo::class.java)
            val pendingIntent = PendingIntent.getActivity(activity,0,intent, 0)
            notificationChannel = NotificationChannel(channelId, description,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(activity, channelId)
                .setContentTitle("Bird Notification:")
                .setContentText("You got mail from your BirdFriend!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.bird_1)

            notificationManager.notify(0, builder.build())
        }
 */
/**
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
MainActivity.sendNewCard = false
}

 */