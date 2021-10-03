package com.gamesploit.crossradar;

import static com.gamesploit.crossradar.GccConfig.urlref.TAG_DEVICEID;
import static com.gamesploit.crossradar.GccConfig.urlref.TAG_DURATION;
import static com.gamesploit.crossradar.GccConfig.urlref.TAG_ERROR;
import static com.gamesploit.crossradar.GccConfig.urlref.TAG_KEY;
import static com.gamesploit.crossradar.GccConfig.urlref.TAG_MSG;
import static com.gamesploit.crossradar.Helper.getUniqueId;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
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

import com.gamesploit.crossradar.GccConfig.urlref;

import com.google.android.material.textfield.TextInputEditText;
import com.scottyab.rootbeer.RootBeer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;


public class LoginActivity extends AppCompatActivity {



    JSONParserString jsonParserString = new JSONParserString();
    private static final String TAG_ISFIRSTSTART = "firstStart";
    TextInputEditText editTextUsername;
    private long getduration;
    private String key;
    TextView version,deadeye;
    private boolean error;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        deadeye = findViewById(R.id.textView2);
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
deadeye.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlref.Deadeye)));
    }
});


        findViewById(R.id.GetkeyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlref.getkey;

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        if(!new RootBeer(LoginActivity.this).isRooted()){
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("No Root Detected")
                    .setMessage("Kindly Use VMOS Or Root Your Device To Ensure Stability While Using App.")
                    .setCancelable(false)
                    .setPositiveButton("ok", (dialog, which) -> finish()).show();
        }else{
            try {
                Process p = Runtime.getRuntime().exec("su");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Android 11 Detected")
                    .setMessage("We Do Not Support Currently")
                    .setCancelable(false)
                    .setPositiveButton("ok", (dialog, which) -> finish()).show();

        }
        isStoragePermissionGranted();


    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
              //  Log.v(TAG,"Permission is granted");
                return true;
            } else {

               // Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
           // Log.v(TAG,"Permission is granted");
            return true;
        }
    }


    private void userLogin() {

        //first getting the values
         key = editTextUsername.getText().toString();
       final String  deviceid = getUniqueId(this);

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
                //creating request parameters
                JSONObject params = new JSONObject();

                String rq = null;
                try {
                    params.put(TAG_DEVICEID,deviceid);
                    params.put(TAG_KEY,key);
              //      params.put(TAG_ONESIGNALID,UUID);
                    rq = jsonParserString.makeHttpRequest(urlref.login, params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //returing the response
                return rq;
            }



            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (lottieDialog != null) {
                    lottieDialog.dismiss();
                }
                if (s == null || s.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject ack = new JSONObject(s);
                            // Log.d("test", String.valueOf(ack));
                            String decData = Helper.profileDecrypt(ack.get("Data").toString(), ack.get("Hash").toString());
                            if (!Helper.verify(decData, ack.get("Sign").toString(), JSONParserString.publickey)) {
                                Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                JSONObject obj = new JSONObject(decData);
                                //  Log.d("test",obj.toString());
                                error = obj.getBoolean(TAG_ERROR);
                                    if (!error) {
                                        getduration = obj.getLong(TAG_DURATION);
                                        if (getduration == 0) {
                                            Toast.makeText(getApplicationContext(), obj.getString(TAG_MSG), Toast.LENGTH_LONG).show();
                                        } else {
                                            //saving to prefrences m
                                            editor.putBoolean(TAG_ISFIRSTSTART, false).apply();
                                            editor.putLong(TAG_DURATION, getduration).apply();
                                            editor.putString(TAG_KEY, key);
                                            editor.apply();
                                            Toast.makeText(getApplicationContext(), obj.getString(TAG_MSG), Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), obj.getString(TAG_MSG), Toast.LENGTH_SHORT).show();
                                    }
                                }
                        }catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}
