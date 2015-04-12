package com.ankitco.foodsnap;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;


public class AddOptionsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_options);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * Starts a search by opening ImageSearchActivity
     * @param view
     */
    public void imageSearch(View view) {
        Intent myIntent = new Intent(AddOptionsActivity.this, ImageSearchActivity.class);
        startActivityForResult(myIntent, 2);
    }

    /**
     * Starts a search with text from editText2
     * @param view
     */
    public void textSearch(View view) {
        Intent myIntent = new Intent(AddOptionsActivity.this, TextSearchActivity.class);
        EditText searchTerm = (EditText)findViewById(R.id.editText2);
        myIntent.putExtra("searchTerm", searchTerm.getText().toString());
        startActivityForResult(myIntent, 2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_options, menu);
        return true;
    }

    @Override
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
            View rootView = inflater.inflate(R.layout.fragment_add_options, container, false);
            return rootView;
        }
    }

    /*
     * Runs to get the name and calories from other Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        String name, calories;
        if(data==null) {
            name = "";
            calories = "";
        } else {
            name = data.getStringExtra("name");
            calories = data.getStringExtra("calories");
        }

        Log.i("A", "AddOptionsActivity");
        // Go back to previous Activity
        Intent intentMessage=new Intent();
        intentMessage.putExtra("name", name);
        intentMessage.putExtra("calories", calories);
        // Set result to 2
        setResult(2,intentMessage);
        finish();
    }
}
