package me.g13n.twitterclient.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import me.g13n.twitterclient.R;
import me.g13n.twitterclient.adapters.TweetsAdapter;
import me.g13n.twitterclient.helpers.BaseJsonHttpResponseHandler;
import me.g13n.twitterclient.clients.TwitterClient;
import me.g13n.twitterclient.clients.TwitterClientApp;
import me.g13n.twitterclient.models.Tweet;

public class TimelineFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        refreshTimeline();
        bindUI();

        return rootView;
    }


    private void bindUI() {
        tweetsAdapter = new TweetsAdapter(getActivity().getBaseContext(), new ArrayList<Tweet>());
        ListView lvTweets = (ListView) rootView.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(tweetsAdapter);
        lvTweets.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    refreshTimeline();
                }
            }
        });
    }


    private void refreshTimeline() {
        TwitterClient twitterClient = TwitterClientApp.getClient();
        twitterClient.getHomeTimeline(lastId,
                new BaseJsonHttpResponseHandler(getActivity().getBaseContext(),
                        getString(R.string.error_service), getString(R.string.error_network)) {

                    @Override
                    public void onSuccess(JSONArray jsonResults) {
                        ArrayList<Tweet> tweets = Tweet.fromJSON(jsonResults);
                        int numItems = tweets.size();
                        if (numItems > 0) {
                    /*
                     * If this is the first time refreshTimeline is being called, the list might
                     * have old tweets from the previous session (restored from the database.)
                     * Just wipe and start over again.
                     */
                            if (!hasRefreshed) {
                                tweetsAdapter.clear();
                                hasRefreshed = true;
                                Log.v("refreshTimeline", "clearing the list ...");
                            }

                            ActiveAndroid.beginTransaction();
                            lastId = tweets.get(numItems - 1).getTweetId();
                            try {
                                for (Tweet tweet : tweets) {
                                    tweetsAdapter.add(tweet);
                                    tweet.save();
                                }
                                tweetsAdapter.notifyDataSetChanged();
                                ActiveAndroid.setTransactionSuccessful();
                            } finally {
                                ActiveAndroid.endTransaction();
                            }
                        }
                    }

                });
    }


    private void preloadTweets() {
        List<Tweet> tweets = TwitterClientApp.getTweets();
        Log.v("preloadTweets", "Returning " + tweets.size() + " tweets from the database");
        for (Tweet tweet : tweets) {
            tweetsAdapter.add(tweet);
        }
        tweetsAdapter.notifyDataSetChanged();
    }


    // State variables
    private long lastId = 0;
    private boolean hasRefreshed = false;


    private View rootView;


    private TweetsAdapter tweetsAdapter;

}
