package me.g13n.twitterclient.helpers;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

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


    public void getHomeTimeline(AsyncHttpResponseHandler handler) {
        String endpoint = getApiUrl("statuses/home_timeline.json");
        client.get(endpoint, handler);
    }

    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */

}