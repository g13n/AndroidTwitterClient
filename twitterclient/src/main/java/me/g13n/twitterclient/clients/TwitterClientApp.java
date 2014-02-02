package me.g13n.twitterclient.clients;

import android.content.Context;

import com.activeandroid.query.Select;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import me.g13n.twitterclient.models.Tweet;

public class TwitterClientApp extends com.activeandroid.app.Application {

	private static Context context;
	
    @Override
    public void onCreate() {
        super.onCreate();
        TwitterClientApp.context = this;
        
        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
        		cacheInMemory().cacheOnDisc().build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .defaultDisplayImageOptions(defaultOptions)
            .build();
        ImageLoader.getInstance().init(config);
    }


    public static TwitterClient getClient() {
    	return (TwitterClient) TwitterClient.getInstance(TwitterClient.class,
                TwitterClientApp.context);
    }

    public static List<Tweet> getTweets() {
        List<Tweet> tweets = new Select().from(Tweet.class).execute();
        return tweets;
    }

}