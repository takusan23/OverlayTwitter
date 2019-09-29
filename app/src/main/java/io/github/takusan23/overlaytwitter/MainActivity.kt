package io.github.takusan23.overlaytwitter

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_button.setOnClickListener {
            //ログイン画面に行く
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        bubbles_button.setOnClickListener {
            //Bubbles表示
            showBubble()
        }
    }

    fun showBubble() {
        val target = Intent(this, BubbleTimelineActivity::class.java)
        val bubbleIntent = PendingIntent.getActivity(this, 0, target, 0)
        //通知作成？
        val bubbleData = Notification.BubbleMetadata.Builder()
            .setDesiredHeight(900)
            .setIcon(Icon.createWithResource(this, R.drawable.ic_home_black_24dp))
            .setIntent(bubbleIntent)
            .build()
        val timelineBot = Person.Builder()
            .setBot(true)
            .setName("TimelineBubble")
            .setImportant(true)
            .build()

        //通知送信
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //通知チャンネル作成
        val notificationId = "tlbubble"
        if (notificationManager.getNotificationChannel(notificationId) == null) {
            //作成
            val notificationChannel = NotificationChannel(
                notificationId, "TimelineBubble",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        //通知作成
        val notification = Notification.Builder(this, notificationId)
            .setContentText("ほかのアプリを起動しながらTwitterを見よう！")
            .setContentTitle("TimelineBubble")
            .setSmallIcon(R.drawable.ic_home_black_24dp)
            .setBubbleMetadata(bubbleData)
            //.addPerson(timelineBot)
            .build()
        //送信
        notificationManager.notify(0,notification)
    }

}
