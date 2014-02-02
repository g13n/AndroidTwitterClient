package me.g13n.twitterclient.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import me.g13n.twitterclient.R;
import me.g13n.twitterclient.fragments.TimelineFragment;

public class TwitterAppActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_app);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new TimelineFragment())
                    .commit();
        }
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
//        refreshTimeline();
    }


    public void onCompose(MenuItem menuItem) {
        Intent composeIntent = new Intent(this, ComposeActivity.class);
        startActivityForResult(composeIntent, COMPOSE_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == COMPOSE_ACTIVITY && resultCode == RESULT_OK) {
//            refreshTimeline();
//        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(final boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }


    private final int COMPOSE_ACTIVITY = 2;
    private final int LOGIN_ACTIVITY = 1;


    private Menu menu;
    private boolean isLoggedIn = true;

}
