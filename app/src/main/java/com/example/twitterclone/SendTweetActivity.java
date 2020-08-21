package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTweetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtTweet;
    private Button btnSendButton, btnShowTweets;
    private ListView showTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtTweet = findViewById(R.id.edt_enter_tweet_send_tweet_activity);
        btnSendButton = findViewById(R.id.btn_send_tweet_send_tweet_activity);
        btnShowTweets = findViewById(R.id.btn_show_users_tweet_send_tweet_activity);
        showTweets = findViewById(R.id.lsitview_send_tweet_activity);

        btnShowTweets.setOnClickListener(this);

        btnSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tweet = edtTweet.getText().toString();
                ParseObject parseObject = new ParseObject("tweets");
                parseObject.put("user", ParseUser.getCurrentUser().getUsername());
                parseObject.put("tweet", tweet);
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null)  {
                            Toast.makeText(getApplicationContext(), "Tweet uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_show_users_tweet_send_tweet_activity) {
            final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
            final SimpleAdapter adapter = new SimpleAdapter(SendTweetActivity.this, tweetList,
                    android.R.layout.simple_list_item_2, new String[] {"tweetUserName", "tweetValue"},
                    new int[] {android.R.id.text1, android.R.id.text2});
            try {
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("tweets");
                parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("following_list"));
                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (objects.size() > 0 && e == null)    {
                            for (ParseObject tweetObject : objects) {
                                HashMap<String, String> userTweet = new HashMap<>();
                                userTweet.put("tweetUserName", tweetObject.getString("user"));
                                userTweet.put("tweetValue", tweetObject.getString("tweet"));
                                tweetList.add(userTweet);
                            }
                            showTweets.setAdapter(adapter);
                        }
                    }
                });

            }   catch (Exception e) {
                e.printStackTrace();
            }
        }



    }
}