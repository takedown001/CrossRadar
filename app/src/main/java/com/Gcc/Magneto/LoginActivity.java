package com.Gcc.Magneto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.Gcc.Magneto.GccConfig.urlref;

import com.Gcc.magneto.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;


public class LoginActivity extends AppCompatActivity {



    private static final String TAG_MSG = "message";
    private static final String TAG_KEY = "key";
    private static final String TAG_ERROR = "error";
    private static final String TAG_DEVICEID = "deviceid";
    private static final String url = urlref.mainurl + "login.php";
    private static final String TAG_DURATION ="duration";
    JSONParserString jsonParserString = new JSONParserString();
    private static final String TAG_ISFIRSTSTART = "firstStart";
    TextInputEditText editTextUsername;
    private long getduration;
    private String key;
    TextView version;
    private static final String TAG = "tag";
    Handler handler = new Handler();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = findViewById(R.id.username);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = findViewById(R.id.verisondisplay);
        String setversion = pInfo.versionName;
        version.setText("version "+setversion);

        findViewById(R.id.signinbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        try {
            Process p = Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
        }

        findViewById(R.id.GetkeyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlref.getkey;

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });



        isStoragePermissionGranted();

    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
          //     Log.v(TAG,"Permission is granted");
                return true;
            } else {

             //   Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
         //   Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {

        String deviceId;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        return deviceId;
    }

    private void userLogin() {

        //first getting the values
         key = editTextUsername.getText().toString();
       final String  deviceid = getDeviceId(this);

        //validating inputs
        if (TextUtils.isEmpty(key)) {
            editTextUsername.setError("Please enter your key");
            editTextUsername.requestFocus();
            return;
        }


        class UserLogin extends AsyncTask<Void, Void, String> {
            final DialogFragment lottieDialog = new LottieDialogFragment().newInstance("loading_state_done.json", true);

            SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);
            SharedPreferences.Editor editor = shred.edit();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                lottieDialog.show(getFragmentManager(), "Loadingdialog");
                lottieDialog.setCancelable(false);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object


                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put(TAG_KEY, key);
                params.put(TAG_DEVICEID, deviceid);
                String rq = jsonParserString.makeHttpRequest(url, params);
                //returing the response
                return rq;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lottieDialog.dismiss();
                        try {

                            JSONObject obj = new JSONObject(s);
                            //    Log.d("login", obj.toString());
                            //checking for error to authenticate
                            if (!obj.getBoolean(TAG_ERROR)) {


                                getduration = obj.getLong(TAG_DURATION);
                                if (getduration == 0) {
                                    Toast.makeText(getApplicationContext(), "SubscriptionExpired", Toast.LENGTH_SHORT).show();
                                } else {
                                    //saving to prefrences m
                                    editor.putBoolean(TAG_ISFIRSTSTART, false).apply();
                                    editor.putLong(TAG_DURATION, getduration).apply();
                                    editor.putString(TAG_KEY, key);
                                    editor.apply();

                                    //    Log.d("date",getcurrentdate);
                                    Toast.makeText(getApplicationContext(), obj.getString(TAG_MSG), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                                    startActivity(intent);

                                    //getting the user from the response.
                                    //starting the profile activity
                                    finish();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString(TAG_MSG), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);
            }
        }


        UserLogin ul = new UserLogin();
        ul.execute();
    }
}
