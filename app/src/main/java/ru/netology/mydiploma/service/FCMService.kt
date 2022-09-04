package ru.netology.mydiploma.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.mydiploma.R
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.dto.PushMessage
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class FCMService: FirebaseMessagingService() {
    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()
    private val action = "action"

    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel_remote_name"
            val descriptionText = "channel_remote_description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onNewToken(token: String) {
        appAuth.sendPushToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val actionResult = message.data[action]
        val localId = appAuth.authLiveData.value?.id ?: 0L

        if (actionResult.isNullOrBlank()) return
        val pushMessage = message.data[content]?.let {
            gson.fromJson(it, PushMessage::class.java)
        }

        pushMessage?.recipientId?.let { recipientId ->
            when {
                recipientId == localId -> handleMessage(pushMessage.recipientId, pushMessage.content)
                recipientId != localId && recipientId == 0L -> appAuth.sendPushToken()
                recipientId != localId && recipientId != 0L -> appAuth.sendPushToken()
                else -> handleMessage(Random.nextLong(100), pushMessage.content)
            }
        }

    }

    private fun handleMessage(id: Long? = null, content: String) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_netology)
            .setContentTitle(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(id?.toInt() ?: Random.nextInt(100), notification)
    }
}