package me.g13n.twitterclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import me.g13n.twitterclient.R;
import me.g13n.twitterclient.fragments.MentionsFragment;
import me.g13n.twitterclient.fragments.TimelineFragment;

public class TwitterAppActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_app);

        setUpTabs();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_compose:
            Intent composeIntent = new Intent(this, ComposeActivity.class);
            startActivityForResult(composeIntent, COMPOSE_ACTIVITY);
            break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COMPOSE_ACTIVITY && resultCode == RESULT_OK) {
            showTimeline();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    protected void showTimeline() {
        TimelineFragment fragment = (TimelineFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentTimeline);
        if (fragment == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new TimelineFragment());
            ft.commit();
        } else {
            fragment.refreshTimeline();
        }
    }


    private void setUpTabs() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        ActionBar.Tab tab;

        tab = actionBar.newTab()
                       .setText(R.string.homeTab)
                       .setTabListener(new TabListener<TimelineFragment>(
                               this, "timeline", TimelineFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                       .setText(R.string.mentionsTab)
                       .setTabListener(new TabListener<MentionsFragment>(
                               this, "mentions", MentionsFragment.class));
        actionBar.addTab(tab);
    }


    private final int COMPOSE_ACTIVITY = 2;

}


class TabListener<T extends Fragment> implements ActionBar.TabListener {

    TabListener(Activity activity, String tag, Class<T> clazz) {
        this.activity = activity;
        this.tag = tag;
        this.clazz = clazz;
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (fragment == null) {
            fragment = Fragment.instantiate(activity, clazz.getName());
            fragmentTransaction.add(R.id.container, fragment, tag);
        } else {
            fragmentTransaction.attach(fragment);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (fragment != null) {
            fragmentTransaction.detach(fragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // Do nothing
    }


    private Fragment fragment;
    private final Activity activity;
    private final String tag;
    private final Class<T> clazz;

}