package me.g13n.twitterclient.helpers;

import com.activeandroid.serializer.TypeSerializer;
import com.activeandroid.util.SQLiteUtils;

import org.json.JSONException;
import org.json.JSONObject;

import me.g13n.twitterclient.models.User;

public class UserSerializer extends TypeSerializer {

    @Override
    public Class<?> getDeserializedType() {
        return User.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return SQLiteUtils.SQLiteType.TEXT.getClass();
    }

    @Override
    public String serialize(Object o) {
        if (o == null) {
            return null;
        }

        return User.toJSON((User) o);
    }

    @Override
    public Object deserialize(Object o) {
        if (o == null) {
            return null;
        }

        try {
            JSONObject jsonObject = new JSONObject((String) o);
            return User.fromJSON(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
