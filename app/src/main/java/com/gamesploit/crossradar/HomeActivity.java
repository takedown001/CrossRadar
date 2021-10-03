package com.gamesploit.crossradar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gamesploit.crossradar.Fragment.HomeFragment;
import com.gamesploit.crossradar.Fragment.NewsFragment;
import com.gamesploit.crossradar.Fragment.SettingFragment;
import com.gamesploit.crossradar.GccConfig.urlref;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import static com.gamesploit.crossradar.GccConfig.urlref.TAG_DEVICEID;
import static com.gamesploit.crossradar.GccConfig.urlref.TAG_ERROR;
import static com.gamesploit.crossradar.GccConfig.urlref.TAG_KEY;
import static com.gamesploit.crossradar.GccConfig.urlref.downloadHexpath;
import static com.gamesploit.crossradar.GccConfig.urlref.update;

public class HomeActivity extends AppCompatActivity {

    public static int REQUEST_OVERLAY_PERMISSION = 5469;
    private String newversion;
    private String data = "data";
    private String whatsNewData,url;
    private boolean ismaintaince,error;
    private static final String TAG_APP_NEWVERSION = "newversion";
    JSONParserString jsonParserString = new JSONParserString();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitRICObverlays();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.home);
        Fragment frag= new HomeFragment();
        loadFragment(frag);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        new OneLoadAllProducts().execute();
        loadAssets();
     //   loadAssets64();
        CheckFloatViewPermission();
        File f = new File(getFilesDir().toString()+urlref.nameoflib);
        if(!f.exists()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new Handler(Looper.getMainLooper()).post(() -> {
                     //   new GetFile(HomeActivity.this  ).execute(downloadHexpath,getFilesDir().toString()+ "/libPOST.so");
                        new GetFile(HomeActivity.this  ).execute(urlref.downloadpath,getFilesDir().toString()+urlref.nameoflib);
                    });
                }
            }).start();

        }
    }
    public void CheckFloatViewPermission()
    {
        if (!Settings.canDrawOverlays(this))
        {
            Toast.makeText(this,"Enable Floating Permission ",Toast.LENGTH_LONG).show();
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        }
    }
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.news:
                    fragment = new NewsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.settings:
                    fragment = new SettingFragment();
                    loadFragment(fragment);
                    return true;

            }
            return false;
        }


    };
    private void InitRICObverlays() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(this)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("This application requires window overlays access permission, please allow first.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    InitRICObverlays();
                }
            }
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



    class OneLoadAllProducts extends AsyncTask<Void, Void, String> {

        SharedPreferences shred = getSharedPreferences("userdetails", MODE_PRIVATE);

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            JSONObject params = new JSONObject();
            String s = null;
            try {
                params.put(TAG_DEVICEID,Helper.getUniqueId(HomeActivity.this));
                params.put(TAG_KEY,shred.getString(TAG_KEY,"null"));
                s= jsonParserString.makeHttpRequest(urlref.login, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return s;
        }


        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null || s.isEmpty()) {
                Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                return ;
            }
            try {
                JSONObject ack = new JSONObject(s);
                String decData = Helper.profileDecrypt(ack.get("Data").toString(), ack.get("Hash").toString());
                if (!Helper.verify(decData, ack.get("Sign").toString(), JSONParserString.publickey)) {
                    Toast.makeText(HomeActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                    return ;
                } else {
                    //converting response to json object
                    JSONObject obj = new JSONObject(decData);
                    Log.d("Log",obj.toString());
                    error = obj.getBoolean(TAG_ERROR);
                    if (error || shred.getString(TAG_KEY,"null").equals("null")) {
                        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                        Toast.makeText(HomeActivity.this ,"Integrity Check Failed",Toast.LENGTH_SHORT).show();
                    }
                    newversion = obj.getString(TAG_APP_NEWVERSION);
                    whatsNewData = obj.getString(data);
                    ismaintaince = obj.getBoolean("ismain");
                    url = obj.getString("updateurl");
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = pInfo.versionName;
                    //    System.out.println("takedown" + "old:" + version + " new:" + newversion);
                    if (Float.parseFloat(version) < Float.parseFloat(newversion)) {
                        Intent intent = new Intent(HomeActivity.this, AppUpdaterActivity.class);
                        intent.putExtra(TAG_APP_NEWVERSION, newversion);
                        intent.putExtra(data, whatsNewData);
                        intent.putExtra("updateurl", url);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (ismaintaince) {
                        Intent intent = new Intent(HomeActivity.this, activityMaintain.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void loadAssets()
    {
        String filepath = Environment.getExternalStorageDirectory()+"/Android/data/.tyb";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filepath);
            byte[] buffer = "DO NOT DELETE".getBytes();
            fos.write(buffer, 0, buffer.length);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String pathf = getFilesDir().toString()+"/lib-native.so";
        try
        {
            OutputStream myOutput = new FileOutputStream(pathf);
            byte[] buffer = new byte[1024];
            int length;
            InputStream myInput = getAssets().open("lib-native.so");
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myInput.close();
            myOutput.flush();
            myOutput.close();

        }

        catch (IOException e)
        {
        }


     String  daemonPath = getFilesDir().toString()+"/lib-native.so";


        try{
            Runtime.getRuntime().exec("chmod 777 "+daemonPath);
        }
        catch (IOException e)
        {
        }

    }

    public void loadAssets64()
    {

        String pathf = getFilesDir().toString()+"/sock64";
        try
        {
            OutputStream myOutput = new FileOutputStream(pathf);
            byte[] buffer = new byte[1024];
            int length;
            InputStream myInput = getAssets().open("sock64");
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myInput.close();
            myOutput.flush();
            myOutput.close();

        }

        catch (IOException e)
        {
        }


        String daemonPath64 = getFilesDir().toString()+"/sock64";


        try{
            Runtime.getRuntime().exec("chmod 777 "+daemonPath64);
        }
        catch (IOException e)
        {
        }

    }

    void Init(){
        SharedPreferences sp=getApplicationContext().getSharedPreferences("espValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed= sp.edit();
        ed.putInt("fps",30);
        ed.putBoolean("Box",true);
        ed.putBoolean("Line",true);
        ed.putBoolean("Distance",false);
        ed.putBoolean("Health",false);
        ed.putBoolean("Name",false);
        ed.putBoolean("Head Position",false);
        ed.putBoolean("Back Mark",false);
        ed.putBoolean("Skelton",false);
        ed.putBoolean("Grenade Warning",false);
        ed.putBoolean("Enemy Weapon",false);
        ed.apply();
    }
    boolean isConfigExist(){
        SharedPreferences sp=getApplicationContext().getSharedPreferences("espValue", Context.MODE_PRIVATE);
        return sp.contains("fps");
    }

}