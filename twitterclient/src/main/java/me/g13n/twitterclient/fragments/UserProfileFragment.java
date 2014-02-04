package me.g13n.twitterclient.fragments;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

import me.g13n.twitterclient.R;
import me.g13n.twitterclient.models.Tweet;
import me.g13n.twitterclient.models.User;

public class UserProfileFragment extends TimelineFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) getArguments().getParcelable("User");
    }

    @Override
    public void refreshTimeline() {
        if (isRefreshing) {
            Log.v("refreshTimeline", "still refreshing ...");
            return;
        }

        isRefreshing = true;

        twitterClient.getUserTimeline(user.getScreenName(), lastId,
                new TimelineRefreshHandler(tweetsAdapter, getActivity().getBaseContext(),
                        getString(R.string.error_service), getString(R.string.error_network)) {

                    @Override
                    public void onSuccess(JSONArray jsonResults) {
                        isRefreshing = false;
                        ArrayList<Tweet> tweets = Tweet.fromJSON(jsonResults);
                        super.onSuccess(jsonResults);
                    }

                    @Override
                    public void onFailure(Throwable error, JSONArray jsonArray) {
                        isRefreshing = false;
                        super.onFailure(error, jsonArray);
                    }
                });
    }

    private User user;

    private boolean isRefreshing = false;

}
