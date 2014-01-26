package me.g13n.twitterclient.models;

import org.json.JSONObject;

public class User extends OurJSONObject {

    User(JSONObject jsonObject) {
        super(jsonObject);
    }

    public static User fromJSON(JSONObject jsonObject) {
        return new User(jsonObject);
    }


    public String getName() {
        return getString("name");
    }

    public String getScreenName() {
        return getString("screen_name");
    }

    public String getProfileImageUrl() {
        return getString("profile_image_url");
    }

    public long getFollowersCount() {
        return getLong("followers_count");
    }

    public long getFriendsCount() {
        return getLong("friends_count");
    }

    public boolean isFollowing() {
        return getBoolean("following");
    }

    public long getId() {
        return getLong("id_str");
    }

}
