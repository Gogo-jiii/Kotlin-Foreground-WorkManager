package com.example.foregroundworkmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyForegroundWork(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    var progress = "Starting work..."
    var NOTIFICATION_ID = 1

    companion object {
        var isStopped = false
    }

    init {
        Companion.isStopped = false
    }

    override fun doWork(): Result {
        setForegroundAsync(showNotification(progress))
        for (i in 0..99) {
            if (Companion.isStopped) {
                break
            }
            progress = "" + i
            Log.d("TAG", progress)
            updateNotification(progress)
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        return Result.success()
    }

    private fun showNotification(progress: String): ForegroundInfo {
        return ForegroundInfo(NOTIFICATION_ID, createNotification(progress))
    }

    private fun createNotification(progress: String): Notification {
        val CHANNEL_ID = "100"
        val title = "Foreground Work"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID, title,
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setSmallIcon(R.drawable.ic_android)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
    }

    private fun updateNotification(progress: String) {
        val notification = createNotification(progress)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}