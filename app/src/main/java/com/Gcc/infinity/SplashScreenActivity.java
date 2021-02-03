package com.Gcc.infinity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.Gcc.infinity.GccConfig.urlref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class SplashScreenActivity extends Activity {

    private static final int SPLASH_SHOW_TIME = 2000;
    private static final String TAG_KEY = "key";
    private static final String TAG_ERROR = "error";
    private static final String TAG_DEVICEID = "deviceid";
    private static final String url = urlref.mainurl + "login.php";
    private static final String TAG_DURATION = "duration";
    //Prefrance
    JSONParserString jsonParserString = new JSONParserString();
    private String key, deviceid;
    private long getduration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
        setContentView(R.layout.activity_splash_screen);
        key = shred.getString(TAG_KEY, "test");
        //   Log.d("key",key);
        new BackgroundSplashTask().execute();

    }


    private boolean checkVPN() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(ConnectivityManager.TYPE_VPN).isConnectedOrConnecting();


    }

    /**
     * Async Task: can be used to load DB, images during which the splash screen
     * is shown to user
     */
    private class BackgroundSplashTask extends AsyncTask<Void, Void, Void> {

        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = shred.edit();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls

            deviceid = LoginActivity.getDeviceId(SplashScreenActivity.this);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put(TAG_KEY, key);
            params.put(TAG_DEVICEID, deviceid);
            String rq = jsonParserString.makeHttpRequest(url, params);

            try {
                Thread.sleep(SPLASH_SHOW_TIME);
                JSONObject obj = new JSONObject(rq);
            //    Log.d("splash", String.valueOf(obj.getBoolean(TAG_ERROR)));
                if (!obj.getBoolean(TAG_ERROR)) {
                    getduration = obj.getLong(TAG_DURATION);

                    editor.putLong(TAG_DURATION, getduration);
                    editor.apply();
                       

                }
            } catch (InterruptedException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Create a new boolean and preference and set it to true
            boolean isFirstStart = shred.getBoolean("firstStart", true);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            if (checkVPN()) {
                Toast.makeText(SplashScreenActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG);
            } else {
                // If the activity has never started before...
                if (isFirstStart) {

                    // Launch app intro
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);

                    // Make a new preferences editor
                    shred.getBoolean("firstStart", false);

                } else {


                    // Create a new boolean and preference and set it to true
                    String isSignedin = shred.getString("key", "user Not Found");

                    if (!isSignedin.equalsIgnoreCase("")) {
                        //user signedin
                        Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                        i.putExtra("loaded_info", " ");
                        startActivity(i);
                    } else {
                        //user not signedin
                        Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        i.putExtra("loaded_info", " ");
                        startActivity(i);
                    }
                }
            }
            finish();
        }


    }
}

