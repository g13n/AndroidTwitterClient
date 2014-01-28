package me.g13n.twitterclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import me.g13n.twitterclient.R;
import me.g13n.twitterclient.helpers.TwitterClient;
import me.g13n.twitterclient.helpers.TwitterClientApp;

public class ComposeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        EditText etStatus = (EditText) findViewById(R.id.etStatus);
        String hint = getString(R.string.status_hint);
        etStatus.setHint(String.format(hint, STATUS_LIMIT));

        errorMessage = Toast.makeText(getBaseContext(), R.string.error_length,
                Toast.LENGTH_SHORT);
        errorShown = false;

        bindUI();
    }

    private void bindUI() {
        EditText etStatus = (EditText) findViewById(R.id.etStatus);
        etStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Button btnCompose = (Button) findViewById(R.id.btnCompose);

                // TODO: will it work for all languages?
                if (editable.length() > STATUS_LIMIT) {
                    btnCompose.setEnabled(false);
                    if (!errorShown) {
                        errorMessage.show();
                        errorShown = true;
                    }
                } else {
                    btnCompose.setEnabled(true);
                    if (errorShown) {
                        errorMessage.cancel();
                        errorShown = false;
                    }
                }
            }
        });
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


    private Toast errorMessage;
    private boolean errorShown;

    /** Twitter status limit */
    private final int STATUS_LIMIT = 140;

}
