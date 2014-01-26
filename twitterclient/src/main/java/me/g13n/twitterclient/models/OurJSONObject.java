package me.g13n.twitterclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class OurJSONObject extends JSONObject {

    OurJSONObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }


    @Override
    public boolean getBoolean(String name) {
        try {
            return jsonObject.getBoolean(name);
        } catch (JSONException ex) {
            return false;
        }
    }

    @Override
    public double getDouble(String name) {
        try {
            return jsonObject.getDouble(name);
        } catch (JSONException ex) {
            return 0.0;
        }
    }

    @Override
    public int getInt(String name) {
        try {
            return jsonObject.getInt(name);
        } catch (JSONException ex) {
            return 0;
        }
    }

    @Override
    public long getLong(String name) {
        try {
            return jsonObject.getLong(name);
        } catch (JSONException ex) {
            return 0;
        }
    }

    @Override
    public String getString(String name) {
        try {
            return jsonObject.getString(name);
        } catch (JSONException ex) {
            return "";
        }
    }


    protected JSONObject jsonObject;

}
