package me.g13n.twitterclient.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

@Table(name = "Users")
public class User extends Model implements Parcelable {

    public User() {
        super();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.registerTypeAdapter(User.class, new UserAdapter()).create();
    }


    public User(Parcel parcel) {
        name = parcel.readString();
        profileImageURL = parcel.readString();
        location = parcel.readString();
        homePage = parcel.readString();
        userId = parcel.readLong();
        numFollowers = parcel.readLong();
        numFriends = parcel.readLong();
        screenName = parcel.readString();
        followersCount = parcel.readLong();
        friendsCount = parcel.readLong();
        description = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(profileImageURL);
        parcel.writeString(location);
        parcel.writeString(homePage);
        parcel.writeLong(userId);
        parcel.writeLong(numFollowers);
        parcel.writeLong(numFriends);
        parcel.writeString(screenName);
        parcel.writeLong(followersCount);
        parcel.writeLong(friendsCount);
        parcel.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[i];
        }
    };


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

    public long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(long followersCount) {
        this.followersCount = followersCount;
    }

    public long getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(long friendsCount) {
        this.friendsCount = friendsCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public static String toJSON(User user) {
        return user.getJSONSerializer().toJson(user, User.class);
    }


    public List<Tweet> tweets() {
        return getMany(Tweet.class, "User");
    }


    public Gson getJSONSerializer() {
        return gson;
    }


    private Gson gson;

    @Column(name = "Name")
    private String name;

    @Column(name = "ProfileImageURL")
    private String profileImageURL;

    @Column(name = "Location")
    private String location;

    @Column(name = "HomePage")
    private String homePage;

    @Column(name = "UserId")
    private long userId;

    @Column(name = "NumFollowers")
    private long numFollowers;

    @Column(name = "NumFriends")
    private long numFriends;

    @Column(name = "ScreenName", index = true)
    private String screenName;

    @Column(name = "FollowersCount")
    private long followersCount;

    @Column(name = "FriendsCount")
    private long friendsCount;

    @Column(name = "Description")
    private String description;

}

class UserAdapter implements JsonSerializer<User> {

    @Override
    public JsonElement serialize(User user, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Name",            user.getName());
        jsonObject.addProperty("ProfileImageURL", user.getProfileImageURL());
        jsonObject.addProperty("Location",        user.getLocation());
        jsonObject.addProperty("HomePage",        user.getHomePage());
        jsonObject.addProperty("UserId",          user.getUserId());
        jsonObject.addProperty("NumFollowers",    user.getNumFollowers());
        jsonObject.addProperty("NumFriends",      user.getNumFriends());
        jsonObject.addProperty("ScreenName",      user.getScreenName());

        return jsonObject;
    }

}
