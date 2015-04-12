//package com.ankitco.foodsnap;
//
//import android.content.Intent;
//import android.support.v7.app.ActionBarActivity;
//import android.support.v7.app.ActionBar;
//import android.support.v4.app.Fragment;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.os.Build;
//import android.widget.EditText;
//import android.widget.TextView;
//
//
//public class ImageSearchActivity extends ActionBarActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_image_search);
//
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_image_search, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        /*
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }*/
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_image_search, container, false);
//            return rootView;
//        }
//    }
//
//

//}
package com.ankitco.foodsnap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ankitco.foodsnap.R;
import com.ankitco.foodsnap.TextSearchActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class ImageSearchActivity extends ActionBarActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    //    public static final int MEDIA_TYPE_IMAGE = 1;
//    private Uri fileUri;
    ImageView image;
    String encodedImage;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
    }

        public void goBack(String name)
    {
        Intent myIntent = new Intent(ImageSearchActivity.this, TextSearchActivity.class);
        myIntent.putExtra("searchTerm", name);
        // Set result to 2
        setResult(2,myIntent);
        startActivityForResult(myIntent, 2);
    }

    public void real_cam(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        }
    }

    String mCurrentPhotoPath;

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "FOOD_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

//    public void real_cam(View view) {
////        Intent intent = new Intent(this, real_cam.class);
////        startActivity(intent);
//
//        // create Intent to take a picture and return control to the calling application
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
////        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
////        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name;
//
//        // start the image capture Intent
//        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
//                Toast.makeText(this, "Image saved to:\n" +
//                        data.getData(), Toast.LENGTH_LONG).show();
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(mCurrentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);


                image = (ImageView) findViewById(R.id.photo_view);
                bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                Bitmap bitmap_scaled = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 5, bitmap.getHeight() / 5, true);

                //Log.d("IMAGEVIEW", data.getData().toString());
                //image.setImageResource(R.drawable.android3d);
                image.setImageBitmap(bitmap_scaled);

                //make bytearray and 64base encoding

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        } else if (requestCode == 2) {
            Log.i("R", "requestCode=2");
            super.onActivityResult(requestCode, resultCode, data);

            String name, calories;
            if(data==null) {
                name = "";
                calories = "";
            } else {
                name = data.getStringExtra("name");
                calories = data.getStringExtra("calories");
            }Log.i("CAL2", calories);

            // Go back to previous Activity
            Intent intentMessage=new Intent();
            intentMessage.putExtra("name", name);
            intentMessage.putExtra("calories", calories);
            // Set result to 2
            setResult(2,intentMessage);
            finish();
        }
    }

    public void open_gallery(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    public String getCode() {
        return encodedImage;
    }

    public void encode(View view) { new SendFoodNameAsyncTask().execute();  }

    public void add_img_food(View view) {
        TextView food = (TextView) findViewById(R.id.food_name);
        if(food.getText().length() != 0) {
            goBack(food.getText().toString());
        }
    }

    private class SendFoodNameAsyncTask extends AsyncTask<String, Void, HttpResponse> {
        @Override
        protected HttpResponse doInBackground(String... params) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Searching...", Toast.LENGTH_SHORT).show();
                }
            });

            ByteArrayOutputStream byte_array = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byte_array);
            byte[] b = byte_array.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

            HttpClient httpclient = new DefaultHttpClient();
            // specify the URL you want to post to
            HttpPost httppost = new HttpPost("https://www.metamind.io/vision/classify");
            httppost.setHeader("Authorization", "Basic aGh5g3x2plGWJVruHMSMJ4bZf4AUgzwub6lU1uHK4He6cG2JUs");

            try {
                // Add your data
                JSONObject object = new JSONObject();

                try {
                    object.put("classifier_id", "food-net");
                    object.put("image_url", ("data:image/jpg;base64," + getCode()));
//                    object.put("image_url", "<img " + "src=\"data:image/jpg;base64," + getCode() + "\" />");
                } catch (Exception ex) {
                    Log.e("MetaMind", "JSON Object Exception");
                }
//                String message = ("<classifier_id>=<food-net>&<image_url>=<img src=\"data:image/jpg;base64," + getCode() + " \"/>");
//                String message = ("classifier_id=food-net&image_url=<img src=\"data:image/jpg;base64," + getCode() + " \"/>");
                String message = object.toString();
//                Log.i("",message);
                httppost.setEntity(new StringEntity(message, "UTF8"));
                HttpResponse response = httpclient.execute(httppost);

                return response;
            } catch (ClientProtocolException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error: Client Protocol Exception", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (UnsupportedEncodingException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error: Unsupported Encoding", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error: I/O Exception", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                });
            }
            return null;
        }
        protected void onPostExecute(HttpResponse result){

            String string = "";
            String line = "";
            int i = 0;

            if (result == null) {
                Toast.makeText(getApplicationContext(), "Error accessing database", Toast.LENGTH_LONG).show();
            } else {
                Scanner sc = null;
                try {
                    sc = new Scanner(result.getEntity().getContent(), "UTF-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (sc.hasNextLine()) {
                    if(i==5) {
                        line = sc.nextLine();
                        string += line;
                    } else {
                        string += sc.nextLine();
                    }
                    i++;
                }

//                Toast.makeText(getApplicationContext(), string , Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "TA DA!", Toast.LENGTH_LONG).show();
                TextView food = (TextView) findViewById(R.id.food_name);

                line = line.replaceAll("\\W","");
                line = line.replace("class_name","");

                food.setText(line);
            }
        }
    }


}

