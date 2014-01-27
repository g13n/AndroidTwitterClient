package me.g13n.twitterclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import me.g13n.twitterclient.helpers.TwitterClient;
import me.g13n.twitterclient.helpers.TwitterClientApp;

public class ComposeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
    }


    public void onComposeSend(View view) {
        EditText etStatus = (EditText) findViewById(R.id.etStatus);
        String status = etStatus.getText().toString();

        if (status == null || status.trim().length() == 0) {
            Toast.makeText(getBaseContext(), R.string.error_empty_status, Toast.LENGTH_SHORT).show();
            return;
        }

        TwitterClient twitterClient = (TwitterClient) TwitterClientApp.getClient();
        twitterClient.postUpdate(status, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject jsonResult) {
                Intent data = new Intent();
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject jsonObject) {
                Toast.makeText(getBaseContext(), R.string.error_compose, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onCancel(View view) {
        Intent data = new Intent();
        setResult(RESULT_CANCELED, data);
        finish();
    }

}
