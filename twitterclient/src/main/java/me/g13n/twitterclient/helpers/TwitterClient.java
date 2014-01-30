package me.g13n.twitterclient.helpers;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.TwitterApi;

import me.g13n.twitterclient.R;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {

    public TwitterClient(Context context) {
        super(context, TwitterApi.class,
                context.getString(R.string.twitter_rest_url),
                context.getString(R.string.twitter_consumer_key),
                context.getString(R.string.twitter_consumer_secret),
                context.getString(R.string.twitter_callback_url));
    }


    /**
     * Return home timeline for the logged in user.
     *
     * @param handler a JsonHttpResponseHandler to handle response
     */
    public void getHomeTimeline(JsonHttpResponseHandler handler) {
        getHomeTimeline(0, handler);
    }

    /**
     * Return home timeline for the logged in user.
     *
     * @param lastId the (since_id) offset to the timeline
     * @param handler a JsonHttpResponseHandler to handle response
     */
    public void getHomeTimeline(long lastId, JsonHttpResponseHandler handler) {
        String endpoint = getApiUrl("statuses/home_timeline.json");
        if (lastId > 0) {
            RequestParams params = new RequestParams();
            params.put("since_id", String.valueOf(lastId));
            client.get(endpoint, params, handler);
        } else {
            client.get(endpoint, handler);
        }
    }


    /**
     * Update user's status.
     *
     * @param tweet   the tweet to update user's status
     * @param handler a JsonHttpResponseHandler to handle response
     */
    public void postUpdate(String tweet, JsonHttpResponseHandler handler) {
        String endpoint = getApiUrl("statuses/update.json");
        RequestParams postData = new RequestParams("status", tweet);
        client.post(endpoint, postData, handler);
    }

}