package com.Gcc.Magneto;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.Gcc.Magneto.Fragment.HomeFragment;
import com.Gcc.Magneto.Fragment.NewsFragment;
import com.Gcc.Magneto.Fragment.SettingFragment;
import com.Gcc.Magneto.GccConfig.urlref;
import com.Gcc.magneto.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private static final String url = urlref.mainurl + "app_updater.php";
    public static int REQUEST_OVERLAY_PERMISSION = 5469;
    // JSON Node names
    private static final String TAG_ERROR = "error";
    private static final String TAG_USERID = "userid";
    private String newversion;
    private String data = "data";
    private String whatsNewData;
    private static final String TAG_APP_NEWVERSION = "newversion";
    private static int backbackexit = 1;

    RequestHandler requestHandler = new RequestHandler();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ShellUtils.SU("su");
        InitRICObverlays();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.home);
        Fragment frag= new HomeFragment();
        loadFragment(frag);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        new OneLoadAllProducts().execute();
        File f = new File(urlref.pathoflib);
        if(!f.exists()){
            new DownloadFile(HomeActivity.this).execute(urlref.downloadpath);
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

    private void StartRICOverlays(){
        startService(new Intent(this, BrutalService.class));
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

        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            HashMap<String, String> params = new HashMap<>();
            params.put(TAG_USERID, String.valueOf(shred.getInt(TAG_USERID,0)));

            return requestHandler.sendPostRequest(url,params);


        }



        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);
                if (!obj.getBoolean(TAG_ERROR))
                {
                    newversion = obj.getString(TAG_APP_NEWVERSION);
                    whatsNewData = obj.getString(data);
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = pInfo.versionName;

                    System.out.println("takedown" + "old:" + version + " new:" + newversion);

                    if (Float.parseFloat(version) < Float.parseFloat(newversion)) {
                        Intent intent = new Intent(HomeActivity.this, AppUpdaterActivity.class);
                        intent.putExtra(TAG_APP_NEWVERSION, newversion);
                        intent.putExtra(data,whatsNewData);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }

            } catch (JSONException | PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}