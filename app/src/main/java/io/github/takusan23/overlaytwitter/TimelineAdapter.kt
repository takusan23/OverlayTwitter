package io.github.takusan23.overlaytwitter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import twitter4j.Status

class TimelineAdapter(var list: ArrayList<Status>) :
    RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tweetTextView = itemView.findViewById<TextView>(R.id.timeline_adapter_tweet_textview)
        var nameTextView = itemView.findViewById<TextView>(R.id.timeline_adapter_name_textview)
        var profileImageView =
            itemView.findViewById<ImageView>(R.id.timeline_adapter_profile_imageview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.timeline_adapter_layout, parent,false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TimelineAdapter.ViewHolder, position: Int) {
        //設定する
        holder.nameTextView.text = list[position].user.name + " / " + list[position].user.screenName
        holder.tweetTextView.text = list[position].text
        //プロフィール
        Glide.with(holder.profileImageView)
            .load(list[position].user.profileImageURLHttps)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
            .into(holder.profileImageView)
    }

}