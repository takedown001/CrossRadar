package com.Gcc.Magneto.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Gcc.Magneto.LoginActivity;
import com.Gcc.magneto.R;


import java.io.DataOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {



    public SettingFragment() {
        // Required empty public constructor
    }
    private SeekBar seekBar;
    private TextView minview;
    private Button logout,play32bit,play64bit;
    private Handler handler = new Handler();
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }


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




        View rootViewone = inflater.inflate(R.layout.fragment_setting, container, false);
        SharedPreferences shred = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);

        SharedPreferences.Editor editor = shred.edit();
        minview = rootViewone.findViewById(R.id.everymin);
        seekBar = rootViewone.findViewById(R.id.seekBar);
        logout = rootViewone.findViewById(R.id.logoutbtn);
        play32bit = rootViewone.findViewById(R.id.playstore32version);
        play64bit = rootViewone.findViewById(R.id.playstore64version);
        SharedPreferences cshred = getActivity().getSharedPreferences("storecolor", MODE_PRIVATE);
        play64bit.setTextColor(cshred.getInt("text64color",getResources().getColor((R.color.whiteTextColor))));
        play64bit.setBackgroundColor(cshred.getInt("bg64color",getResources().getColor((R.color.orange))));
        play32bit.setTextColor(cshred.getInt("textcolor32",getResources().getColor((R.color.orange))));
        play32bit.setBackgroundColor(cshred.getInt("bg32color",getResources().getColor((R.color.whiteTextColor))));



        play32bit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences cshred = getActivity().getSharedPreferences("storecolor", MODE_PRIVATE);
                SharedPreferences.Editor ceditor = cshred.edit();
                play32bit.setBackgroundColor(getResources().getColor((R.color.whiteTextColor)));
                play32bit.setTextColor(getResources().getColor((R.color.orange)));
                play64bit.setBackgroundColor(getResources().getColor((R.color.orange)));
                play64bit.setTextColor(getResources().getColor(R.color.whiteTextColor));
                ceditor.putInt("textcolor32",getResources().getColor(R.color.orange));
                ceditor.putInt("bg32color",getResources().getColor((R.color.whiteTextColor)));
                ceditor.putInt("text64color",getResources().getColor((R.color.whiteTextColor)));
                ceditor.putInt("bg64color",getResources().getColor(R.color.orange));
                ceditor.apply();
                editor.putString("version","32");
                editor.apply();


            }
        });
        play64bit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Currently,This Feature is unavailable",Toast.LENGTH_LONG);
                SharedPreferences cshred = getActivity().getSharedPreferences("storecolor", MODE_PRIVATE);
                SharedPreferences.Editor ceditor = cshred.edit();
                play64bit.setBackgroundColor(getResources().getColor(R.color.whiteTextColor));
                play64bit.setTextColor(getResources().getColor(R.color.orange));
                play32bit.setBackgroundColor(getResources().getColor((R.color.orange)));
                play32bit.setTextColor(getResources().getColor((R.color.whiteTextColor)));
                ceditor.putInt("textcolor32",getResources().getColor(R.color.whiteTextColor));
                ceditor.putInt("bg32color",getResources().getColor((R.color.orange)));
                ceditor.putInt("text64color",getResources().getColor((R.color.orange)));
                ceditor.putInt("bg64color",getResources().getColor(R.color.whiteTextColor));
                ceditor.apply();
                editor.putString("version","64");
                editor.apply();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                minview.setText("Every "+progress+" Minutes");
               handler.postDelayed(new Runnable() {
                   @Override
                   public void run() {


                       try {
                           Process su = Runtime.getRuntime().exec("su");
                           DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                           outputStream.writeBytes("touch /sdcard/m.txt"+"\n");
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
               },progress*1000*60);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences shred = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
                shred.edit().clear().apply();
                getActivity().finish();
                Toast.makeText(getActivity(), "Logged Out Successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        return rootViewone;
    }


}