## Yet another Android Twitter App client

This is an Android Twitter client that supports home timelines and composing new tweets.  It does
not support user timelines, retweets and direct messages yet.

This repository contains the complete Android Studio workspace.

### How to get it working?

  * Import the project into [Android Studio](http://tools.android.com/download/studio)
  * Create `res/values/twitter_auth.xml` with the following contents:


    <?xml version="1.0" encoding="utf-8"?>
    <resources>

        <string name="twitter_rest_url">https://api.twitter.com/1.1</string>
        <string name="twitter_consumer_key">YOUR CONSUMER KEY</string>
        <string name="twitter_consumer_secret">YOUR CONSUMER SECRET</string>
        <string name="twitter_callback_url">oauth://g13n-twitter-app</string>

    </resources>

  * Choose _Build > Rebuild Project_ and _Run_
