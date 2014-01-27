package me.g13n.twitterclient.helpers;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Date;
import java.util.List;

import me.g13n.twitterclient.R;
import me.g13n.twitterclient.models.Tweet;
import me.g13n.twitterclient.models.User;

public class TweetsAdapter extends ArrayAdapter<Tweet> {

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = getContext();
        View view = convertView;
        ImageLoader imgLoader = ImageLoader.getInstance();

        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.tweet_item, null);
            imgLoader.init(ImageLoaderConfiguration.createDefault(context));
        }

        Tweet tweet = getItem(position);
        User user   = tweet.getUser();

        if (view != null) {
            ImageView ivProfile = (ImageView) view.findViewById(R.id.ivProfile);
            imgLoader.displayImage(user.getProfileImageUrl(), ivProfile);

            TextView tvName = (TextView) view.findViewById(R.id.tvName);
            tvName.setText(Html.fromHtml(user.getName()));

            TextView tvId = (TextView) view.findViewById(R.id.tvId);
            String screenName = String.format(context.getString(R.string.screen_name_format),
                    user.getScreenName());
            tvId.setText(Html.fromHtml(screenName));

            TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
            Date time = tweet.getTime();
            String friendlyTime = (String) DateUtils.getRelativeDateTimeString(context,
                    time.getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0);
            tvTime.setText(friendlyTime);

            TextView tvText = (TextView) view.findViewById(R.id.tvText);
            tvText.setText(Html.fromHtml(tweet.getText()));
        }

        return view;
    }

}
