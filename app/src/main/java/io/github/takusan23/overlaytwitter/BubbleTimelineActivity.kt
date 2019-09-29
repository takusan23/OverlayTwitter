package io.github.takusan23.overlaytwitter

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_bubble_timeline.*
import twitter4j.Paging
import twitter4j.Status
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import kotlin.concurrent.thread

class BubbleTimelineActivity : AppCompatActivity() {

    val statusList = arrayListOf<Status>()
    lateinit var adapter:TimelineAdapter
    lateinit var pref_setting:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bubble_timeline)

        pref_setting = PreferenceManager.getDefaultSharedPreferences(this)

        //recyclerview
        timeline_activity_recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        timeline_activity_recyclerview.layoutManager = layoutManager
        adapter = TimelineAdapter(statusList)
        timeline_activity_recyclerview.adapter = adapter

        loadStatus()

        //再読み込みボタン
        timeline_activity_load_button.setOnClickListener { loadStatus() }

    }

    private fun loadStatus(){
        val consumerKey =pref_setting.getString("consumer_key","")
        val consumerSecret = pref_setting.getString("consumer_secret","")
        val token = pref_setting.getString("token","")
        val tokenSecret = pref_setting.getString("token_secret","")

        //アクセストークンの設定とか
        val twitter = TwitterFactory().instance
        val accessToken = AccessToken(token,tokenSecret)
        twitter.setOAuthConsumer(consumerKey,consumerSecret)
        twitter.oAuthAccessToken=accessToken

        //タイムライン取得
        thread {
            try{
                val paging = Paging()
                paging.count=200
                val status = twitter.getHomeTimeline(paging)
                for (i in status){
                    println(i.text)
                    statusList.add(i)
                }
                //UI更新
                runOnUiThread{
                    adapter.notifyDataSetChanged()
                }
            }catch (e:TwitterException) {
                e.printStackTrace()
            }
        }

    }
}
