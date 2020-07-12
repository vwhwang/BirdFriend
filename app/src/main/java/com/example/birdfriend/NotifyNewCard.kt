package com.example.birdfriend

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotifyNewCard(val context: Context){
    private  val channelId = "com.example.birdfriend"
    private  val description = "BirdFriend Notification"

    fun sendAddPostNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Bird Notification:")
            .setContentText("You got mail from your BirdFriend!")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.bird_1)

        with (NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }
    }

}




// NOTIFICATION WITHIN FRAGMENT
/*
    private  val channelId = "com.example.birdfriend"
    private  val description = "BirdFriend Notification"

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
//NOTIFICATION WITHIN MAIN
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