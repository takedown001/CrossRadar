package com.Gcc.infinity.Fragment;

import android.annotation.SuppressLint;


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.Gcc.infinity.GccConfig.urlref;
import com.Gcc.infinity.JavaUrlConnectionReader;
import com.Gcc.infinity.LoginActivity;
import com.Gcc.infinity.BrutalService;
import com.Gcc.infinity.SafeService;
import com.Gcc.infinity.ShellUtils;
import com.Gcc.infinity.R;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static android.content.Context.MODE_PRIVATE;


public class GlobalFragment extends Fragment {


    private final JavaUrlConnectionReader reader = new JavaUrlConnectionReader();
    private String data;

    String Antibangl1 = urlref.mainurl+ "Antibangl1.php";
    String Antibangl2 = urlref.mainurl + "Antibangl2.php";
    private   String version,deviceid;
    Handler handler = new Handler();
    private static final String TAG_DEVICEID = "deviceid";
    private static final String TAG_VERSION = "version";
    ImageView taptoactivategl1;
    public GlobalFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

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
        View rootViewone = inflater.inflate( R.layout.fragment_global, container, false);
        SharedPreferences shred = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);

        final File daemon = new File(urlref.pathoflib);

        deviceid = LoginActivity.getDeviceId(getActivity());
        version = shred.getString("version","32");
        Button cleanguest, fixgame, antiban1, antiban2;
        antiban1 = rootViewone.findViewById(R.id.servergl1);
        antiban2 = rootViewone.findViewById(R.id.servergl2);
        cleanguest = rootViewone.findViewById(R.id.globalcleanguest);
        fixgame = rootViewone.findViewById(R.id.globalfixgame);
        taptoactivategl1 = rootViewone.findViewById(R.id.taptoactivategl);
        final DialogFragment lottieDialog = new LottieDialogFragment().newInstance("loadingdone.json",true);
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

                antiban.show(getActivity().getFragmentManager(),"antiban1");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        antiban.dismiss();
                        if (daemon.exists()){
                            ShellUtils.SU("chmod 777 " + daemon);
                            getActivity().startService(new Intent(getActivity(), SafeService.class));
                        } else {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("Requirement Not Found, Restart Your Device")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                        HashMap<String, String> params = new HashMap<>();
                        params.put(TAG_VERSION, version);
                        params.put(TAG_DEVICEID, deviceid);
                        data = reader.getUrlContents(Antibangl1, params);
                      //  Log.d("data", data);
                        try {

                            Process su = Runtime.getRuntime().exec("su");
                            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                            outputStream.writeBytes(data + "\n");
                            outputStream.flush();
                            outputStream.writeBytes("exit\n");
                            outputStream.flush();
                            su.waitFor();


                        } catch (IOException e) {
                            try {
                                throw new Exception(e);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        } catch (InterruptedException e) {
                            try {
                                throw new Exception(e);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }


                    }


                },4000);

            }
        });


        antiban2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                antiban.show(getActivity().getFragmentManager(),"antiban2");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    antiban.dismiss();
                    if (daemon.exists()){
                        ShellUtils.SU("chmod 777 " + daemon);
                        getActivity().startService(new Intent(getActivity(), BrutalService.class));
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("Requirement Not Found, Restart Your Device")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                    HashMap<String, String> params = new HashMap<>();
                    params.put(TAG_VERSION,version);
                    params.put(TAG_DEVICEID, deviceid);
                    data =   reader.getUrlContents(Antibangl2,params);
                    try {
                        Process su = Runtime.getRuntime().exec("su");
                        DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                        outputStream.writeBytes(data + "\n");
                        outputStream.flush();
                        outputStream.writeBytes("exit\n");
                        outputStream.flush();
                        su.waitFor();
                    } catch (IOException e) {
                        try {
                            throw new Exception(e);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        try {
                            throw new Exception(e);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }

                }
            },4000);

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
                                                      try {
                                                          Process su = Runtime.getRuntime().exec("su");
                                                          DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                                                          outputStream.writeBytes("Target=\"/data/data/com.vng.pubgmobile/shared_prefs/device_id.xml\"\n" +
                                                                  "if [ \"$(pidof com.vng.pubgmobile)\" != \"\" ]\n" +
                                                                  "then\n" +
                                                                  "su -c killall com.vng.pubgmobile\n" +
                                                                  "fi\n" +
                                                                  " rm -rf $Target\n" +
                                                                  " touch $Target\n" +
                                                                  " chmod 777 $Target\n" +
                                                                  "echo \"\"\n" +
                                                                  "echo \"<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n" +
                                                                  "<map>\n" +
                                                                  "    <string name=\\\"random\\\"></string>\n" +
                                                                  "    <string name=\\\"install\\\"></string>\n" +
                                                                  "    <string name=\\\"uuid\\\">$(tr -dc a-z0-9 </dev/urandom | head -c 32)</string>\n" +
                                                                  "</map> \" >> $Target\n" +
                                                                  "rm -rf /data/data/com.vng.pubgmobile/databases\n" +
                                                                  "rm -rf /data/media/0/Android/data/com.vng.pubgmobile/files/login-identifier.txt\n" +
                                                                  "chmod 644 $Target\n");
                                                          outputStream.flush();
                                                          outputStream.writeBytes("exit\n");
                                                          outputStream.flush();
                                                          su.waitFor();
                                                      } catch (IOException e) {
                                                          try {
                                                              throw new Exception(e);
                                                          } catch (Exception exception) {
                                                              exception.printStackTrace();
                                                          }
                                                      } catch (InterruptedException e) {
                                                          try {
                                                              throw new Exception(e);
                                                          } catch (Exception exception) {
                                                              exception.printStackTrace();
                                                          }
                                                      }



                                                  }
                                              }, 4000);
                                              //   new DownloadTask(getActivity(), URL)
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
                        try {
                            Process su = Runtime.getRuntime().exec("su");
                            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                            outputStream.writeBytes("rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile/files/ProgramBinaryCache &>/dev/null\n" +
                                    "rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile\n" +
                                    "chmod 755 /data/media/0/Android/data/com.vng.pubgmobile\n" +
                                    "chmod 755 /data/media/0/Android/data/com.vng.pubgmobile/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.vng.pubgmobile/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.vng.pubgmobile/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.vng.pubgmobile/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.vng.pubgmobile/*/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.vng.pubgmobile/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.vng.pubgmobile/*/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/media/0/Android/data/com.vng.pubgmobile/*/*/*/*/*/*/*/*\n" +
                                    "rm -rf /storage/emulated/0/Android/data/com.vng.pubgmobile\n" +
                                    "chmod 755 /data/data/com.vng.pubgmobile\n" +
                                    "chmod 755 /data/data/com.vng.pubgmobile/*\n" +
                                    "chmod 755 /data/data/com.vng.pubgmobile/*/*\n" +
                                    "chmod 755 /data/data/com.vng.pubgmobile/*/*/*\n" +
                                    "chmod 755 /data/data/com.vng.pubgmobile/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.vng.pubgmobile/*/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.vng.pubgmobile/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.vng.pubgmobile/*/*/*/*/*/*/*\n" +
                                    "chmod 755 /data/data/com.vng.pubgmobile/*/*/*/*/*/*/*/*\n" +
                                    "rm -rf /data/data/com.vng.pubgmobile\n" +
                                    "pm install -r /data/app/com.vng.pubgmobile*/base.apk\n");
                            outputStream.flush();
                            outputStream.writeBytes("exit\n");
                            outputStream.flush();
                            su.waitFor();
                        } catch (IOException e) {
                            try {
                                throw new Exception(e);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        } catch (InterruptedException e) {
                            try {
                                throw new Exception(e);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }

                    }
                },4000);
                //   new DownloadTask(getActivity(), URL);

            }
        });

        taptoactivategl1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Currently Team is Working",Toast.LENGTH_LONG).show();
            }
        });

        return rootViewone;
    }




}

