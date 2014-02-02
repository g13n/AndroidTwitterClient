package me.g13n.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

@Table(name = "Tweets")
public class Tweet extends Model {

    public long getTweetId() {
        return tweetId;
    }

    public void setTweetId(long tweetId) {
        this.tweetId = tweetId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = new Date(createdAt);
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean isFavorited) {
        this.isFavorited = isFavorited;
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public void setRetweeted(boolean isRetweeted) {
        this.isRetweeted = isRetweeted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();

        try {
            tweet.setTweetId(jsonObject.getLong("id"));
            tweet.setCreatedAt(jsonObject.getString("created_at"));
            tweet.setText(jsonObject.getString("text"));
            tweet.setFavorited(jsonObject.getBoolean("favorited"));
            tweet.setRetweeted(jsonObject.getBoolean("retweeted"));
            tweet.setUser(User.fromJSON(jsonObject.getJSONObject("user")));
        } catch (JSONException e) {
            tweet = null;
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSON(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;

            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (JSONException ex) {
                continue;
            }

            if (jsonObject != null) {
                Tweet tweet = Tweet.fromJSON(jsonObject);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            }
        }

        return tweets;
    }


    @Column(name = "tweetId")
    private long tweetId;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "text")
    private String text;

    @Column(name = "isFavorited")
    private boolean isFavorited;

    @Column(name = "isRetweeted")
    private boolean isRetweeted;

    @Column(name = "user")
    private User user;

}
