package com.internetpolice.app.waring

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.internetpolice.core.network.dto.DomainDetailsDto

private const val CHANNEL_ID = "internet_police_channel"
private const val CHANNEL_NAME = "Internet police notification channel"
private const val CHANNEL_DESCRIPTION = "This notification channel dedicated for internet police"
const val NOTIFICATION_CLICKED_DOMAIN_DETAILS_DTO = "notificationDomainDetailsDto"
fun showWarningNotification(
    domainDetailsDto: DomainDetailsDto,
    context: Context,
    title: String,
    body: String,
    @DrawableRes smallIconDrawable: Int,
    largeImageDrawable: Bitmap
) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Create notification channel for API 26+
    val channel = NotificationChannel(
        CHANNEL_ID,
        CHANNEL_NAME,
        NotificationManager.IMPORTANCE_HIGH
    )
    channel.description = CHANNEL_DESCRIPTION
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        channel.setAllowBubbles(true)
    }
    channel.enableLights(true)
    channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
    channel.enableVibration(true)
    channel.shouldShowLights()
    notificationManager.createNotificationChannel(channel)

    //Check notification to content
    val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
        context,
        CHANNEL_ID
    )
    val notificationIntent = Intent(context, Class.forName(context.packageName + ".MainActivity"))
    notificationIntent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    notificationIntent.putExtra(NOTIFICATION_CLICKED_DOMAIN_DETAILS_DTO, domainDetailsDto)
    val pendingIntent: PendingIntent? = PendingIntent.getActivity(
        context, 0,
        notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
    )

    notificationBuilder.setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .setContentTitle(title)
        .setContentText(body)
        .setSmallIcon(smallIconDrawable)
        .setTicker(title)
        .setLargeIcon(largeImageDrawable)
        .setFullScreenIntent(pendingIntent, true)
        .priority = NotificationCompat.PRIORITY_MAX
    notificationManager.notify(0, notificationBuilder.build())
}



