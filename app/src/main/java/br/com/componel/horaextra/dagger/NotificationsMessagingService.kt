package br.com.componel.horaextra.dagger

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import br.com.componel.horaextra.R
import br.com.componel.horaextra.ui.activity.MainActivity
import com.google.firebase.messaging.RemoteMessage
import com.pusher.pushnotifications.fcm.MessagingService


class NotificationsMessagingService : MessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("NotificationsService", "From: " + remoteMessage.from)
        Log.d("NotificationsService", "From: " + remoteMessage.notification!!.body)
        Log.i("NotificationsService", "Got a remote message 🎉")
        val title = "HORA EXTRA"
        sendNotificationData(title, remoteMessage.notification!!.body.toString()) //send notification to user

    }

    private fun sendNotificationData(messageTitle: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pattern = longArrayOf(500, 500, 500, 500, 500)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.grupois_icon)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE, 1, 1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                as NotificationCompat.Builder

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}