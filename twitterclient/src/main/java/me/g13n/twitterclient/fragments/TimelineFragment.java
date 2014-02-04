package me.g13n.twitterclient.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import me.g13n.twitterclient.R;
import me.g13n.twitterclient.activities.UserProfileActivity;
import me.g13n.twitterclient.adapters.TweetsAdapter;
import me.g13n.twitterclient.clients.TwitterClient;
import me.g13n.twitterclient.clients.TwitterClientApp;
import me.g13n.twitterclient.helpers.BaseJsonHttpResponseHandler;
import me.g13n.twitterclient.models.Tweet;

public class TimelineFragment extends Fragment {

    /**
     * Handler when a tweet is clicked.
     */
    public interface OnTweetClickListener {
        public void onTweetClick(Tweet tweet);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        bindUI();
        refreshTimeline();

        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnTweetClickListener) {
            tweetClickListener = (OnTweetClickListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTweetClickListener");
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_timeline, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
        case R.id.action_refresh:
            refreshTimeline();
            break;
        case R.id.action_profile:
            showUserProfile();
            break;
        default:
            return super.onOptionsItemSelected(menuItem);
        }

        return true;
    }


    private void bindUI() {
        twitterClient = TwitterClientApp.getClient();
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

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Tweet tweet = (Tweet) adapterView.getItemAtPosition(position);
                tweetClickListener.onTweetClick(tweet);
            }
        });
    }


    public void refreshTimeline() {
        if (isRefreshing) {
            Log.v("refreshTimeline", "still refreshing ...");
            return;
        }

        isRefreshing = true;

        twitterClient.getHomeTimeline(lastId,
                new TimelineRefreshHandler(tweetsAdapter, true, getActivity().getBaseContext(),
                        getString(R.string.error_service), getString(R.string.error_network)) {

                    @Override
                    public void onSuccess(JSONArray jsonResults) {
                        isRefreshing = false;
                        ArrayList<Tweet> tweets = Tweet.fromJSON(jsonResults);
                        int numItems = tweets.size();
                        lastId = tweets.get(numItems - 1).getTweetId();
                        super.onSuccess(jsonResults);
                    }

                    @Override
                    public void onFailure(Throwable error, JSONArray jsonArray) {
                        isRefreshing = false;
                        super.onFailure(error, jsonArray);
                    }
                });
    }


    public void showUserProfile() {
        Intent userProfile = new Intent(getActivity(), UserProfileActivity.class);
        startActivity(userProfile);
    }


    private void preloadTweets() {
        List<Tweet> tweets = TwitterClientApp.getTweets();
        for (Tweet tweet : tweets) {
            tweetsAdapter.add(tweet);
        }
        tweetsAdapter.notifyDataSetChanged();
    }


    // State variables
    protected long lastId = 0;
    private boolean isRefreshing = false;


    protected View rootView;


    protected TwitterClient twitterClient;
    protected TweetsAdapter tweetsAdapter;

    protected OnTweetClickListener tweetClickListener;

}


class TimelineRefreshHandler extends BaseJsonHttpResponseHandler {

    public TimelineRefreshHandler(TweetsAdapter tweetsAdapter, Context context,
            String genericError, String networkError) {
        super(context, genericError, networkError);
        this.tweetsAdapter = tweetsAdapter;
    }

    public TimelineRefreshHandler(TweetsAdapter tweetsAdapter, boolean shouldSave,
            Context context, String genericError, String networkError) {
        this(tweetsAdapter, context, genericError, networkError);
        this.shouldSave = shouldSave;
    }


    @Override
    public void onSuccess(JSONArray jsonResults) {
        ArrayList<Tweet> tweets = Tweet.fromJSON(jsonResults);
        /*
         * If this is the first time refreshTimeline is being called, the list might
         * have old tweets from the previous session (restored from the database.)
         * Just wipe and start over again.
         */
        if (!isFirstTime) {
            tweetsAdapter.clear();
            isFirstTime = true;
            Log.v("refreshTimeline", "clearing the list ...");
        }

        if (shouldSave) {
            ActiveAndroid.beginTransaction();
        }
        try {
            for (Tweet tweet : tweets) {
                tweetsAdapter.add(tweet);
                if (shouldSave) {
                    tweet.save();
                }
            }
            tweetsAdapter.notifyDataSetChanged();
            if (shouldSave) {
                ActiveAndroid.setTransactionSuccessful();
            }
        } finally {
            if (shouldSave) {
                ActiveAndroid.endTransaction();
            }
        }
    }


    private boolean shouldSave = false;
    private boolean isFirstTime = false;

    private TweetsAdapter tweetsAdapter;

}