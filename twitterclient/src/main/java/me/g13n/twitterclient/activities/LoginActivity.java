package me.g13n.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.oauth.OAuthLoginActivity;

import me.g13n.twitterclient.R;
import me.g13n.twitterclient.helpers.TwitterClient;

public class LoginActivity extends OAuthLoginActivity<TwitterClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}


	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
    @Override
    public void onLoginSuccess() {
    	Intent mainActivity = new Intent(this, TwitterAppActivity.class);
//        mainActivity.putExtra("login", true);
    	startActivity(mainActivity);
    }
    
    // OAuth authentication flow failed, handle the error
    // i.e Display an error dialog or toast
    @Override
    public void onLoginFailure(Exception e) {
        Log.d("DEBUG", "Error logging in to Twitter!");
        e.printStackTrace();
    }

    
    // Click handler method for the button used to start OAuth flow
    // Uses the client to initiate OAuth authorization
    // This should be tied to a button used to login
    public void loginToTwitter(View view) {
        getClient().connect();
    }

}
