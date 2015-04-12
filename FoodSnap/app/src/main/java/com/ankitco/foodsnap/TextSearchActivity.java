package com.ankitco.foodsnap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TextSearchActivity extends ActionBarActivity {

    public HashMap<String, Integer> foodList;

    String searchTerm;
    TextView textv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_search);

        foodList = new HashMap<String, Integer>();
        foodList.put("waffles", 100);
        foodList.put("icecream", 150);
        foodList.put("donuts", 200);
        foodList.put("pizza", 300);
        foodList.put("hamburger", 350);
        foodList.put("clubsandwich", 600);

        if (savedInstanceState == null) {
            Intent extras = getIntent();
            if (extras == null) {
                searchTerm = null;
            } else {
                searchTerm = extras.getStringExtra("searchTerm");
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        } else {
            searchTerm = (String) savedInstanceState.getSerializable("searchTerm");
        }
    }

    @Override
    protected void onStart() {
        String calories;
        super.onStart();

        // Set search term textbox
        textv = (TextView) findViewById(R.id.textViewSearchTerm);
        if (searchTerm != null && searchTerm.length() > 0) {
            textv.setText(searchTerm);
        } else {
            textv.setText("No Search Term.");
        }

        if (foodList.containsKey(searchTerm.toLowerCase())) {
            calories = foodList.get(searchTerm.toLowerCase()).toString();
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Calorie count not found", Toast.LENGTH_LONG).show();
                }
            });
            calories = "0";
        }
        ((EditText)findViewById(R.id.editText)).setText(calories);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text_search, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_text_search, container, false);
            return rootView;
        }
    }

    /**
     * Go back to previous activity, saving the name and calories
     *
     * @param V
     */
    public void goBackAdd(View V) {
        goBack(searchTerm, ((EditText)findViewById(R.id.editText)).getText().toString());
    }

    /**
     * Go back to previous Activity because user cancelled
     *
     * @param V
     */
    public void goBackCancel(View V) {
        goBack("", "0");
    }

    /**
     * Go back to previous Activity
     *
     * @name Name of food
     * @calories Calorie count of food
     */
    public void goBack(String name, String calories) {
        Intent intentMessage = new Intent();

        intentMessage.putExtra("name", name);
        intentMessage.putExtra("calories", calories);
        // Set result to 2
        setResult(2, intentMessage);
        finish();
    }

}

