package com.gamesploit.crossradar;

import static com.gamesploit.crossradar.GccConfig.urlref.TAG_DEVICEID;
import static com.gamesploit.crossradar.GccConfig.urlref.TAG_DURATION;
import static com.gamesploit.crossradar.GccConfig.urlref.TAG_ERROR;
import static com.gamesploit.crossradar.GccConfig.urlref.TAG_KEY;
import static com.gamesploit.crossradar.GccConfig.urlref.TAG_MSG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.gamesploit.crossradar.GccConfig.urlref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class SplashScreenActivity extends Activity {

    private static final int SPLASH_SHOW_TIME = 2000;
    //Prefrance
    JSONParserString jsonParserString = new JSONParserString();
    public static String key, deviceid;
    private long getduration = 0;
    public boolean error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
        setContentView(R.layout.activity_splash_screen);
        key = shred.getString(TAG_KEY, "null");
        //   Log.d("key",key);
        new BackgroundSplashTask().execute();
        isStoragePermissionGranted();
    }





    public boolean isStoragePermissionGranted() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            //  Log.v(TAG,"Permission is granted");
            return true;
        } else {

            // Log.v(TAG,"Permission is revoked");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, 1);
            return false;
        }
    }

    /**
     * Async Task: can be used to load DB, images during which the splash screen
     * is shown to user
     */
    private class BackgroundSplashTask extends AsyncTask<Void, Void, String> {

        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = shred.edit();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls
            deviceid = Helper.getUniqueId(SplashScreenActivity.this);
        }

        @Override
        protected String doInBackground(Void... arg0) {
            //creating request parameters
            JSONObject params = new JSONObject();
            String rq = null;
            try {
                params.put(TAG_KEY, key);
                params.put(TAG_DEVICEID, deviceid);
                rq = jsonParserString.makeHttpRequest(urlref.login, params);
                //converting response to json object
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rq;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Create a new boolean and preference and set it to true
            boolean isFirstStart = shred.getBoolean("firstStart", true);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (s == null || s.isEmpty()) {
                Toast.makeText(SplashScreenActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject ack = new JSONObject(s);
                String decData = Helper.profileDecrypt(ack.get("Data").toString(), ack.get("Hash").toString());
                if (!Helper.verify(decData, ack.get("Sign").toString(), JSONParserString.publickey)) {
                    Toast.makeText(SplashScreenActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    JSONObject obj = new JSONObject(decData);
                    //   Log.d("login", obj.toString());
                    error = obj.getBoolean(TAG_ERROR);
                    if (!error) {
                        getduration = obj.getLong(TAG_DURATION);
                        editor.putLong(TAG_DURATION, getduration);
                        editor.apply();
                        //    Log.d("splash", String.valueOf(getduration));
                    }

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
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

