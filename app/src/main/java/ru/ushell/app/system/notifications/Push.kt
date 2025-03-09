package ru.ushell.app.system.notifications

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.ushell.app.MainActivity
import ru.ushell.app.R
import ru.ushell.app.ui.screens.timetableScreen.TimeTableScreen

class Push {

    fun sendNotification(context: Context) {
        val intent = Intent(
            context,
            MainActivity::class.java
        )
        val requestCode = System.currentTimeMillis().toInt()
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(context)

        val pendingIntent: PendingIntent = PendingIntent
            .getActivity(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setSmallIcon(R.mipmap.logo_app_al)
            .setContentTitle("notification.title)")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("notification.body")
            )
            .setShowWhen(true)
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            notify(123, builder.build())
        }
    }
}