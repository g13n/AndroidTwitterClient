package me.g13n.twitterclient.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONObject;

import me.g13n.twitterclient.R;
import me.g13n.twitterclient.clients.TwitterClient;
import me.g13n.twitterclient.clients.TwitterClientApp;
import me.g13n.twitterclient.fragments.UserProfileFragment;
import me.g13n.twitterclient.helpers.BaseJsonHttpResponseHandler;
import me.g13n.twitterclient.models.User;

public class UserProfileActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        showUserProfile();
    }


    private void showUserProfile() {
        Context context = getBaseContext();
        final Activity activity = this;

        TwitterClient twitterClient = TwitterClientApp.getClient();

        final ImageLoader imgLoader = ImageLoader.getInstance();
        imgLoader.init(ImageLoaderConfiguration.createDefault(context));

        twitterClient.getMyDetails(new BaseJsonHttpResponseHandler(context,
                getString(R.string.error_service), getString(R.string.error_network)) {

            @Override
            public void onSuccess(JSONObject jsonObject) {

                User loggedInUser = User.fromJSON(jsonObject);

                String profileImageUrl = loggedInUser.getProfileImageURL();
                if (profileImageUrl != null || profileImageUrl != "") {
                    ImageView ivUserProfile = (ImageView) findViewById(R.id.ivUserProfile);
                    imgLoader.displayImage(profileImageUrl, ivUserProfile);
                }

                TextView tvProfileName = (TextView) findViewById(R.id.tvProfileName);
                tvProfileName.setText(loggedInUser.getName());

                TextView tvProfileDesc = (TextView) findViewById(R.id.tvProfileDesc);
                tvProfileDesc.setText(loggedInUser.getDescription());

                TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
                tvFollowers.setText(String.format(getString(R.string.followers_count_format),
                        loggedInUser.getFollowersCount()));

                TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
                tvFollowing.setText(String.format(getString(R.string.following_count_format),
                        loggedInUser.getFriendsCount()));

                activity.setTitle(String.format(getString(R.string.screen_name_format),
                        loggedInUser.getScreenName()));

                Bundle args = new Bundle();
                args.putParcelable("User", loggedInUser);

                UserProfileFragment userProfileFragment = new UserProfileFragment();
                userProfileFragment.setArguments(args);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                        .beginTransaction();
                fragmentTransaction.replace(R.id.flUserProfile, userProfileFragment);
                fragmentTransaction.commit();

            }

        });
    }

}
