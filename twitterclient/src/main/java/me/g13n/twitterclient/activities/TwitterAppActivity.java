package me.g13n.twitterclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import me.g13n.twitterclient.R;
import me.g13n.twitterclient.adapters.TweetsAdapter;
import me.g13n.twitterclient.helpers.BaseJsonHttpResponseHandler;
import me.g13n.twitterclient.helpers.TwitterClient;
import me.g13n.twitterclient.helpers.TwitterClientApp;
import me.g13n.twitterclient.models.Tweet;

public class TwitterAppActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_app);

        bindUI();
        preloadTweets();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        if (isLoggedIn) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.login_menu, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_login:
            Intent loginActivity = new Intent(this, LoginActivity.class);
            startActivity(loginActivity);
            break;
        case R.id.action_logout:
            isLoggedIn = false;
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Update the action bar depending upon the user's logged in state
     *
     * @param menu the instance of the ActionBar menu
     */
    protected void updateActionBar(Menu menu) {
        if (isLoggedIn) {
            Log.d("DEBUG", "Updating menu to main ...");
            getMenuInflater().inflate(R.menu.main_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.login_menu, menu);
        }

        if (Build.VERSION.SDK_INT >= 11) {
            invalidateOptionsMenu();
        } else {
            ActivityCompat.invalidateOptionsMenu(this);
        }
    }


    public void onRefresh(MenuItem item) {
        refreshTimeline();
    }


    public void onCompose(MenuItem menuItem) {
        Intent composeIntent = new Intent(this, ComposeActivity.class);
        startActivityForResult(composeIntent, COMPOSE_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COMPOSE_ACTIVITY && resultCode == RESULT_OK) {
            refreshTimeline();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(final boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }


    private void preloadTweets() {
        List<Tweet> tweets = TwitterClientApp.getTweets();
        Log.v("preloadTweets", "Returning " + tweets.size() + " tweets from the database");
        for (Tweet tweet : tweets) {
            tweetsAdapter.add(tweet);
        }
        tweetsAdapter.notifyDataSetChanged();
    }

    private void refreshTimeline() {
        TwitterClient twitterClient = (TwitterClient) TwitterClientApp.getClient();
        twitterClient.getHomeTimeline(lastId,
                new BaseJsonHttpResponseHandler(getBaseContext(), getString(R.string.error_service),
                        getString(R.string.error_network)) {

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


    private void bindUI() {
        tweetsAdapter = new TweetsAdapter(this, new ArrayList<Tweet>());
        ListView lvTweets = (ListView) findViewById(R.id.lvTweets);
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


    private final int COMPOSE_ACTIVITY = 2;
    private final int LOGIN_ACTIVITY = 1;


    private TweetsAdapter tweetsAdapter;

    private long lastId = 0;
    private Menu menu;
    private boolean isLoggedIn = true;
    private boolean hasRefreshed = false;

}
