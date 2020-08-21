package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class SocialMediaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private List<String> userList = new ArrayList<>();
    private ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        listView = findViewById(R.id.list_view_soical_media_activity);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, userList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);


        getUsers();
    }

    private void getUsers() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Users...");
        progressDialog.show();
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().get("username"));
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (objects.size() > 0 && e == null)    {
                    for (ParseUser user : objects)  {
                        userList.add(user.getUsername());
                    }
                    progressDialog.dismiss();
                    listView.setAdapter(adapter);

                    for (String followers : userList)   {
                        if (ParseUser.getCurrentUser().getList("following_list") != null)   {
                            if (ParseUser.getCurrentUser().getList("following_list").contains(followers))   {
                                listView.setItemChecked(userList.indexOf(followers), true);
                            }
                        }
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout_user)   {
            ParseUser.logOut();
            finish();
            Intent intent = new Intent(SocialMediaActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.send_tweet_item)   {
            Intent intent = new Intent(SocialMediaActivity.this, SendTweetActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;
        if (checkedTextView.isChecked())    {
            Toast.makeText(getApplicationContext(), "You now follow " + userList.get(position), Toast.LENGTH_SHORT).show();
            ParseUser.getCurrentUser().add("following_list", userList.get(position));
        }   else    {
            Toast.makeText(getApplicationContext(), "You now do not follow " + userList.get(position), Toast.LENGTH_SHORT).show();
            ParseUser.getCurrentUser().getList("following_list").remove(userList.get(position));
            List currectUserFollowingList = ParseUser.getCurrentUser().getList("following_list");
            ParseUser.getCurrentUser().remove("following_list");
            ParseUser.getCurrentUser().put("following_list", currectUserFollowingList);
        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)  {
                    Toast.makeText(getApplicationContext(), "Changes are saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}