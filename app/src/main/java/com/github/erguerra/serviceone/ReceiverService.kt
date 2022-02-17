package com.github.erguerra.serviceone

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class ReceiverService : Service() {

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForeground(3, createNotification())
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
        if (EXPECTED_INTENT_TYPE == intent.type) {
            val receivedJson = intent.getStringExtra(Intent.EXTRA_TEXT)
            val receivedModel = getModelFromJson(receivedJson?:"")
            receivedModel?.let {
                showResultAndCloseService(receivedModel)
            }
        }
    }
    return START_REDELIVER_INTENT
}

    private fun getModelFromJson(json: String): ReceivedData? {
        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<ReceivedData> = moshi.adapter(ReceivedData::class.java)
        return jsonAdapter.fromJson(json)
    }

    private fun showResultAndCloseService(receivedModel: ReceivedData) {
        Toast.makeText(applicationContext, "Name: ${receivedModel.name} Age: ${receivedModel.age}", Toast.LENGTH_LONG).show()
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification() : Notification {
        var builder = NotificationCompat.Builder(this, createNotificationChannel("ServiceOne", "ServiceOneChannel"))
        return builder.build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val channel = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return channelId
    }

    companion object {
        const val EXPECTED_INTENT_TYPE: String = "text/json"
    }
}