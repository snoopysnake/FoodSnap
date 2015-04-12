package com.ankitco.foodsnap;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    EditText caloriesEditText, nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Log.i("C", "Create");
        // Clear saved data
        SharedPreferences settings = getSharedPreferences(getString(R.string.action_settings), MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        //Save Value
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        caloriesEditText = (EditText)findViewById(R.id.editText);
        nameEditText = (EditText)findViewById(R.id.editText2);

        SharedPreferences settings = getSharedPreferences(getString(R.string.action_settings), MODE_PRIVATE);

        // Set to default value or restore the saved value
        caloriesEditText.setText(settings.getString(getString(R.string.cal), "0"));
        nameEditText.setText(settings.getString(getString(R.string.prev_food), "N/A"));
        Log.i("S", "Start");
    }

    @Override
         protected void onResume() {
        super.onResume();
        Log.i("R", "Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("P", "P");
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveNameAndCalories();
        Log.i("S", "Stop");
    }

    /**
     * Saves most recent food and calorie count to SharedPreferences
     */
    public void saveNameAndCalories()
    {
        SharedPreferences settings = getSharedPreferences(getString(R.string.action_settings), MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        //Save Value
        editor.putString(getString(R.string.cal), caloriesEditText.getText().toString());
        editor.putString(getString(R.string.prev_food), nameEditText.getText().toString());
        editor.commit();
    }

    /*
     * Runs to get the name and calories from other Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(data==null)
        {return;}
        else {
            String calories = data.getStringExtra("calories");
            if (caloriesEditText.getText().toString() == null ||
                    caloriesEditText.getText().toString().length() == 0)
                caloriesEditText.setText("0");
            // Increase the calorie count
            int current = Integer.parseInt(caloriesEditText.getText().toString());
            Log.i("C", current+"");
            caloriesEditText.setText((current + Integer.parseInt(calories)) + "");

            String name = data.getStringExtra("name");
            // Set the message string in textView
            if (name != null || name.length() > 0)
                nameEditText.setText(name);

            saveNameAndCalories();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_about:
                new AlertDialog.Builder(this).setTitle("About")
                    .setMessage("Created at Bitcamp 2015.\nAnkit Bhandary, Alex Chiang, Andrew Wang, Kevin Schechter")
                    .setNeutralButton("Close", null).show();
                return true;
        }

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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /**
     * Opens the Add Options Activity
     * @param view
     */
    public void addFoodOptions(View view) {
        Intent myIntent = new Intent(MainActivity.this, AddOptionsActivity.class);
        startActivityForResult(myIntent, 2);
    }

    /**
     * Clear saved recent food and calorie count
     * @param view
     */
    public void clearSavedData(View view) {
        clearSavedData();
    }

    /**
     * Clear saved recent food and calorie count
     */
    private void clearSavedData() {
        caloriesEditText.setText("0");
        nameEditText.setText("None");
        // Clear saved data
        SharedPreferences settings = getSharedPreferences(getString(R.string.action_settings), MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        //Save Value
        editor.commit();
    }
}
