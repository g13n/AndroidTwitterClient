package me.g13n.twitterclient.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tweet extends OurJSONObject {

    Tweet(JSONObject jsonObject) {
        super(jsonObject);
    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet(jsonObject);

        try {
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException ex) {
            tweet.user = null;
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
                tweets.add(tweet);
            }
        }

        return tweets;
    }


    public long getId() {
        return getLong("id");
    }

    public String getText() {
        return getString("text");
    }

    public boolean isFavorited() {
        return getBoolean("favorited");
    }

    public boolean isRetweeted() {
        return getBoolean("retweeted");
    }

    public User getUser() {
        return user;
    }


    private User user;

}
