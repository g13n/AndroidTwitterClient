package me.g13n.twitterclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import me.g13n.twitterclient.R;
import me.g13n.twitterclient.helpers.TweetsAdapter;
import me.g13n.twitterclient.helpers.TwitterClientApp;
import me.g13n.twitterclient.models.Tweet;
import me.g13n.twitterclient.helpers.TwitterClient;

public class TwitterAppActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_app);

        displayTimeline();
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


    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(final boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }


    protected void displayTimeline() {
        TwitterClient twitterClient = (TwitterClient) TwitterClientApp.getClient();
        twitterClient.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray jsonResults) {
                ArrayList<Tweet> tweets = Tweet.fromJSON(jsonResults);
                ListView lvTweets = (ListView) findViewById(R.id.lvTweets);
                TweetsAdapter tweetsAdapter = new TweetsAdapter(getBaseContext(), tweets);
                lvTweets.setAdapter(tweetsAdapter);
            }
        });
    }


    private final int LOGIN_REQUEST = 9;


    private Menu menu;
    private boolean isLoggedIn = true;

}
