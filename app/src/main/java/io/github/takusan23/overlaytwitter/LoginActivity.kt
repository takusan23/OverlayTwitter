package io.github.takusan23.overlaytwitter

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_login.*
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.auth.RequestToken
import twitter4j.conf.ConfigurationBuilder
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity() {

    var consumerKey = ""
    var consumerSecret = ""
    lateinit var twitterFactory: TwitterFactory
    lateinit var twitter: Twitter
    lateinit var request_token: RequestToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        consumerKey = getString(R.string.consumer_key)
        consumerSecret = getString(R.string.consumer_secret)

        loginScreen()
        getAccessToken()
    }

    //ログイン画面を表示させる
    fun loginScreen() {
        show_login_button.setOnClickListener {
            val cb = ConfigurationBuilder()
            cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(null)
                .setOAuthAccessTokenSecret(null)

            twitterFactory = TwitterFactory(cb.build())
            twitter = twitterFactory.getInstance()
            thread {
                try {
                    request_token = twitter.getOAuthRequestToken()
                    val url = request_token.getAuthenticationURL()
                    val i = Intent(Intent.ACTION_VIEW, url.toUri());
                    startActivity(i);
                } catch (e: TwitterException) {
                    e.printStackTrace()
                }
            }
        }
    }

    //アクセストークン取得
    fun getAccessToken() {
        access_token_button.setOnClickListener {
            val pin = login_pin_code_textinput.text.toString()
            thread {
                val accessToken = twitter.getOAuthAccessToken(pin)
                //ログインできた
                val token = accessToken.token
                val secret = accessToken.tokenSecret
                //保存
                val pref_setting = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = pref_setting.edit()
                editor.putString("consumer_key",consumerKey)
                editor.putString("consumer_secret",consumerSecret)
                editor.putString("token",token)
                editor.putString("token_secret",secret)
                editor.apply()
                //Activity終了
                finish()
            }
        }
    }

}
