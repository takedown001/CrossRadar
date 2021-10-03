package com.gamesploit.crossradar.Fragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.gamesploit.crossradar.FloatLogo;
import com.gamesploit.crossradar.GccConfig.urlref;
import com.gamesploit.crossradar.Helper;
import com.gamesploit.crossradar.JSONParserString;
import com.gamesploit.crossradar.ShellUtils;
import com.gamesploit.crossradar.R;
import com.scottyab.rootbeer.RootBeer;


import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static android.content.Context.MODE_PRIVATE;
import static com.gamesploit.crossradar.Helper.givenToFile;

import org.json.JSONObject;


public class KoreanFragment extends Fragment  {

    public KoreanFragment() {
        // Required empty public constructor
    }
    JSONParserString jsonParserString = new JSONParserString();
    private String data, version;
    ImageView taptoactivatekr;
    Handler handler = new Handler();
    private String gameName = "com.pubg.krmobile";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    final DialogFragment lottieDialog = new LottieDialogFragment().newInstance("loadingdone.json",true);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        View rootViewone = inflater.inflate(R.layout.fragment_korean, container, false);
        SharedPreferences shred = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);

        version = shred.getString("version","32");
        Button cleanguest, fixgame, antiban1, antiban2;
        cleanguest = rootViewone.findViewById(R.id.koreancleanguest);
        fixgame = rootViewone.findViewById(R.id.koreanfixgame);
        antiban1 = rootViewone.findViewById(R.id.serverkr1);
        antiban2 = rootViewone.findViewById(R.id.serverkr2);
        taptoactivatekr = rootViewone.findViewById(R.id.taptoactivatekr);


        lottieDialog.setCancelable(false);
        final DialogFragment antiban = new LottieDialogFragment().newInstance("antiban.json",true);
        antiban.setCancelable(false);

        final DialogFragment fixgameani = new LottieDialogFragment().newInstance("settings.json",true);
        lottieDialog.setCancelable(false);
        final DialogFragment cleanguestani = new LottieDialogFragment().newInstance("tick-confirm.json",true);
        antiban.setCancelable(false);


        antiban1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.appInstalledOrNot(gameName,getActivity())){
                    Toast.makeText(getActivity(), "Game Not Installed", Toast.LENGTH_LONG).show();
                }
                else {
                    antiban.show(getActivity().getFragmentManager(), "StartCheatGl");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            antiban.dismiss();
                            ShellUtils.SU("iptables -F");
                            ShellUtils.SU("iptables --flush");
                            if(new RootBeer(getActivity()).isRooted()) {
                                try {
                                    Helper.unzip(getActivity(),gameName);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                cheat("startcheatkr");
                                startPatcher();
                            }
                            else {
                                Toast.makeText(getActivity(),"Root Access Was Not Granted ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, 2000);
                }
            }
        });

        antiban2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.appInstalledOrNot(gameName,getActivity())){
                    Toast.makeText(getActivity(), "Game Not Installed", Toast.LENGTH_LONG).show();
                }
                else {
                    antiban.show(getActivity().getFragmentManager(), "StartCheatGl");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            antiban.dismiss();
                            ShellUtils.SU("iptables -F");
                            ShellUtils.SU("iptables --flush");
                            if(new RootBeer(getActivity()).isRooted()) {
                                try {
                                    Helper.unzip(getActivity(),gameName);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                cheat("stopcheatkr");
                             //   startPatcher();
                            } else {
                                Toast.makeText(getActivity(),"Root Access Was Not Granted ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, 2000);
                }
            }
        });


        cleanguest.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SdCardPath")
            @Override
            public void onClick(View view) {
                cleanguestani.show(getActivity().getFragmentManager(),"cleanguest");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cleanguestani.dismiss();
                        String s = "rm -Rf /data/data/com.pubg.krmobile/databases\n" +
                                "rm -Rf /data/data/com.pubg.krmobile/shared_prefs/gsdk_prefs.xml\n" +
                                "rm -Rf /data/data/com.pubg.krmobile/shared_prefs/APMCfg.xml\n" +
                                "rm -Rf /data/data/com.pubg.krmobile/shared_prefs/device_id.xml\n" +
                                "echo -n \"<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\\n<map>\\n    <string name=\\\"random\\\"></string>\\n    <string name=\\\"install\\\"></string>\\n    <string name=\\\"uuid\\\">$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM$RANDOM</string>\\n</map>\" > /data/data/com.pubg.krmobile/shared_prefs/device_id.xml\n" +
                                "chmod -R 555 /data/data/com.pubg.krmobile/shared_prefs/device_id.xml\n" +
                                "rm -Rf /data/media/0/.backups\n" +
                                "rm -Rf /data/media/0/Android/data/com.pubg.krmobile/cache\n" +
                                "rm -Rf /data/media/0/Android/data/com.pubg.krmobile/files/login-identifier.txt\n" +
                                "rm -Rf /data/media/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Intermediate/SaveGames/JKGuestRegisterCnt.json\n" +
                                "find /storage/emulated/0 -type f \\( -name \".fff\" -o -name \".zzz\" -o -name \".system_android_l2\" \\) -exec rm -Rf {} \\;";
                        new Thread(() -> {
                            String[] lines = s.split(Objects.requireNonNull(System.getProperty("line.separator")));
                            for (int i = 0; i < lines.length; i++) {

                                //      Log.d("testlines", lines[i]);
                                try {
                                    ShellUtils.SU(lines[i]);
                                    TimeUnit.MILLISECONDS.sleep(20);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                },2000);
                //   new DownloadTask(getActivity(), URL);


            }
        });

        fixgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixgameani.show(getActivity().getFragmentManager(),"fixgame");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fixgameani.dismiss();
                        String s = "rm -Rf /data/data/com.pubg.krmobile/lib\n" +
                                "rm -Rf /data/data/com.pubg.krmobile/databases__hs_log_store\n" +
                                "touch /data/data/com.pubg.krmobile/__hs_log_store\n" +
                                "chmod 444 /data/data/com.pubg.krmobile/__hs_log_store\n" +
                                "chmod 555 /data/data/com.pubg.krmobile/files/tss*\n" +
                                "chmod 555 /data/data/com.pubg.krmobile/files/tersafe.update\n" +
                                "echo \"8192\" > /proc/sys/fs/inotify/max_user_watches\n" +
                                "echo \"8192\" > /proc/sys/fs/inotify/max_user_instances\n" +
                                "echo \"8192\" > /proc/sys/fs/inotify/max_queued_events\n" +
                                "rm -rf /sdcard/Android/data/com.google.backup\n"+
                                "rm -Rf /data/media/0/Android/data/.um /data/media/0/Android/data/pushSdk /data/media/0/Android/obj /data/media/0/.backups /data/media/0/MidasOversea /data/media/0/tencent\n" +
                                "find /storage/emulated/0 -type f \\( -name \".fff\" -o -name \".zzz\" -o -name \".system_android_l2\" \\) -exec rm -Rf {} \\;\n" +
                                "pm install -i com.android.vending /data/app/com.pubg.krmobile-*/*.apk >/dev/null";
                        new Thread(() -> {
                            String[] lines = s.split(Objects.requireNonNull(System.getProperty("line.separator")));
                            for (String line : lines) {

                                //      Log.d("testlines", lines[i]);
                                try {
                                    ShellUtils.SU(line);
                                    TimeUnit.MILLISECONDS.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                },2000);
                //   new DownloadTask(getActivity(), URL);


            }
        });
        return rootViewone;
    }

    public void cheat(String file){


        class load extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {

                    JSONObject ack = new JSONObject(s);
                    String decData = Helper.profileDecrypt(ack.get("Data").toString(), ack.get("Hash").toString());
                    if (!Helper.verify(decData, ack.get("Sign").toString(), JSONParserString.publickey)) {
                        Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        JSONObject obj = new JSONObject(decData);
                        data = obj.getString("data");
                        Helper.givenToFile(getActivity(),data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(Void... voids) {
                JSONObject params = new JSONObject();
                String rq = null;
                try {

                    params.put("file", file);
                    rq = jsonParserString.makeHttpRequest(urlref.server, params);
                } catch(Exception e){
                    e.printStackTrace();
                }

                return rq;
            }
        }
        new load().execute();
    }


    void startPatcher() {
        if (!Settings.canDrawOverlays(getActivity())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" +getActivity().getPackageName ()));
            startActivityForResult(intent, 123);
        } else {
            startFloater();
        }
    }

    private void startFloater() {
        getActivity().stopService(new Intent(getActivity(), FloatLogo.class));
        Intent i = new Intent(getActivity(),FloatLogo.class);
        i.putExtra("gametype",version);
        i.putExtra("gamename",2);
        getActivity().startService(i);
    }
}


