package com.internetpolice.app.waring

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.internetpolice.app.MainActivity
import com.internetpolice.app.R


private const val CHANNEL_ID = "internet_police_channel_scrap"
private const val CHANNEL_NAME = "Internet police notification channel_scrap"
private const val CHANNEL_DESCRIPTION = "This notification channel dedicated for internet police"
const val CANCEL_NOTIFICATION = "CANCEL_NOTIFICATION"
const val NOTIFICATION_ID = 522

fun scrapNotificationUtils(
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


    val notificationIntent = Intent(context, NotificationReceiver::class.java)
    notificationIntent.putExtra("notification_id", NOTIFICATION_ID)
    notificationIntent.action = CANCEL_NOTIFICATION
    val pendingIntent =
        PendingIntent.getBroadcast(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )



    notificationBuilder.setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .setSmallIcon(smallIconDrawable)
        .setColor(Color.BLUE)
        .setColorized(true)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setCustomContentView(
            RemoteViews(
                context.packageName,
                R.layout.custom_notification_layout
            ).apply {
                setTextViewText(R.id.titleText, title)
                setTextViewText(R.id.messageText, Html.fromHtml(body))
                setImageViewBitmap(R.id.imageView, largeImageDrawable)
                setOnClickPendingIntent(R.id.actionButton, pendingIntent)

                when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        setTextColor(R.id.titleText, Color.WHITE)
                        setTextColor(R.id.messageText, Color.GRAY)
                    }

                    Configuration.UI_MODE_NIGHT_NO -> {
                        setTextColor(R.id.titleText, Color.BLACK)
                        setTextColor(R.id.messageText, Color.GRAY)
                    }

                    else -> {

                    }
                }

            }
        )
        //.addAction(R.id.actionButton, "Got It", pendingIntent)
        .setStyle(
            NotificationCompat.DecoratedCustomViewStyle()
        ).build().flags = Notification.FLAG_AUTO_CANCEL

    notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
}