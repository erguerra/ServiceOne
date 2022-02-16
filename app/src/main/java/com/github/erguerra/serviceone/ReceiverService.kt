package com.github.erguerra.serviceone

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

class ReceiverService : Service() {

    override fun onBind(intent: Intent): IBinder? = null


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

    companion object {
        const val EXPECTED_INTENT_TYPE: String = "text/json"
    }
}