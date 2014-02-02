package me.g13n.twitterclient.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.client.HttpResponseException;
import org.json.JSONArray;

public class BaseJsonHttpResponseHandler extends JsonHttpResponseHandler {

    public BaseJsonHttpResponseHandler(Context context, String genericError, String networkError) {
        this.appContext = context;
        this.genericError = genericError;
        this.networkError = networkError;
    }

    @Override
    public void onFailure(Throwable error, JSONArray jsonArray) {
        Log.e("handleFailureMessage", error.toString());
        Toast.makeText(appContext, genericError, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void handleFailureMessage(Throwable error, String s) {
        HttpResponseException response = (HttpResponseException) error;
        if (response.getStatusCode() > 400) {
            Log.e("handleFailureMessage", error.toString());
            Toast.makeText(appContext, genericError, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(appContext, networkError, Toast.LENGTH_SHORT).show();
        }
    }

    private String networkError;
    private String genericError;
    private Context appContext;

}
