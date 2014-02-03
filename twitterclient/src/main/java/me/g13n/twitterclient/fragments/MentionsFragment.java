package me.g13n.twitterclient.fragments;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

import me.g13n.twitterclient.R;
import me.g13n.twitterclient.models.Tweet;

public class MentionsFragment extends TimelineFragment {

    @Override
    public void refreshTimeline() {
        if (isRefreshing) {
            Log.v("refreshTimeline", "still refreshing ...");
            return;
        }

        isRefreshing = true;

        twitterClient.getMentionsTimeline(lastId,
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


    private boolean isRefreshing = false;

}
