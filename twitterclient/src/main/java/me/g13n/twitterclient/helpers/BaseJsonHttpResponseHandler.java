package me.g13n.twitterclient.helpers;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

public class BaseJsonHttpResponseHandler extends JsonHttpResponseHandler {

    public BaseJsonHttpResponseHandler(Context context, String genericError, String networkError) {
        this.appContext = context;
        this.genericError = genericError;
        this.networkError = networkError;
    }

    @Override
    public void onFailure(Throwable error, JSONArray jsonArray) {
        Toast.makeText(appContext, genericError, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void handleFailureMessage(Throwable error, String s) {
        Toast.makeText(appContext, networkError, Toast.LENGTH_SHORT).show();
    }

    private String networkError;
    private String genericError;
    private Context appContext;

}
