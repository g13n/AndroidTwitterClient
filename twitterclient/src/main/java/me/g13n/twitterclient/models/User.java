package me.g13n.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@Table(name = "Users")
public class User extends Model {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getNumFollowers() {
        return numFollowers;
    }

    public void setNumFollowers(long numFollowers) {
        this.numFollowers = numFollowers;
    }

    public long getNumFriends() {
        return numFriends;
    }

    public void setNumFriends(long numFriends) {
        this.numFriends = numFriends;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }


    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();

        try {
            user.setName(jsonObject.getString("name"));
            user.setProfileImageURL(jsonObject.getString("profile_image_url"));
            user.setLocation(jsonObject.getString("location"));
            user.setHomePage(jsonObject.getString("url"));
            user.setUserId(jsonObject.getLong("id"));
            user.setNumFollowers(jsonObject.getLong("followers_count"));
            user.setNumFriends(jsonObject.getLong("friends_count"));
            user.setScreenName(jsonObject.getString("screen_name"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return user;
    }


    public List<Tweet> tweets() {
        return getMany(Tweet.class, "user");
    }


    @Column(name = "name")
    private String name;

    @Column(name = "profileImageURL")
    private String profileImageURL;

    @Column(name = "location")
    private String location;

    @Column(name = "homePage")
    private String homePage;

    @Column(name = "userId")
    private long userId;

    @Column(name = "numFollowers")
    private long numFollowers;

    @Column(name = "numFriends")
    private long numFriends;

    @Column(name = "screenName", index = true)
    private String screenName;

}
