package com.Gcc.Magneto;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;


import com.Gcc.Magneto.GccConfig.urlref;
import com.Gcc.magneto.R;

import java.io.File;

public class SafeService  extends Service {

    private PowerManager.WakeLock mWakeLock;

    public static final String LOG_TAG = "takedown";

    private View mainView;

    private WindowManager windowManagerMainView;

    private WindowManager.LayoutParams paramsMainView;

    private LinearLayout layout_main_view;

    private RelativeLayout layout_icon_control_view;

    private int WideViewValue = 360;
    private int highViewValue = 220;
    private String myDaemon = "./"+ urlref.pathoflib;

    private Intent pubgmintent;

    @Override
    public IBinder onBind(Intent p1) {
        return null;
    }

    @SuppressLint({"InvalidWakeLockTag", "WakelockTimeout"})
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mWakeLock == null){
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOG_TAG);
            mWakeLock.acquire();
        }
        return START_NOT_STICKY;
    }

    private void ShowMainView() {
        mainView = LayoutInflater.from(this).inflate(R.layout.activity_safeservice, null);
        paramsMainView = getParams();
        windowManagerMainView = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManagerMainView.addView(mainView, paramsMainView);
        InitShowMainView();
    }
    Boolean isInBackground;
    private void InitShowMainView() {

        layout_icon_control_view = mainView.findViewById(R.id.layout_icon_control_view);
        layout_main_view = mainView.findViewById(R.id.layout_main_view);
        LinearLayout layout_close_main_view = mainView.findViewById(R.id.layout_close_main_view);
        layout_close_main_view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View p1) {
                layout_main_view.setVisibility(View.GONE);
                layout_icon_control_view.setVisibility(View.VISIBLE);
            }
        });
        RelativeLayout layout_view = mainView.findViewById(R.id.layout_view);
        layout_view.setOnTouchListener(onTouchListener());
        InitBaseView();

    }
    private void InitBaseView() {
        Button btn_exit = mainView.findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View p1) {
                stopSelf();
            }
        });


        TextView tv_ric = mainView.findViewById(R.id.textview_ric);
        SeekBar seekbar_wideview = mainView.findViewById(R.id.seekbar_wideview);
        final TextView textview_seekbar = mainView.findViewById(R.id.textview_wideview);


        tv_ric.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View p1) {
                AlertDialog alertDialog = new AlertDialog.Builder(getBaseContext())
                        .setTitle("Contact Us")
                        .setMessage("gcc-org.com/about/")
                        .setPositiveButton("OK", null)
                        .create();

                alertDialog.getWindow().setType(getLayoutType());
                alertDialog.getWindow().setGravity(Gravity.TOP);
                alertDialog.show();
            }
        });







        Switch smallcroshair = mainView.findViewById(R.id.smallcroshair);
        smallcroshair.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    ShellUtils.SU(myDaemon + " 5");
                } else {
                    ShellUtils.SU(myDaemon + " 6");
                }
            }

        });

        seekbar_wideview.incrementProgressBy(90);
        seekbar_wideview.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar p1, int progress, boolean p3) {
                progress = progress / 90;
                progress = progress * 90;
                textview_seekbar.setText(String.valueOf(progress));


            }

            @Override
            public void onStartTrackingTouch(SeekBar p1) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar p1) {
                int progress = p1.getProgress();
                progress = progress / 90;
                progress = progress * 90;
                if (progress == 0){
                    WideViewValue = 15;
                } else if(progress == 90){
                    WideViewValue = 16;
                } else if(progress == 180){
                    WideViewValue = 17;
                } else if(progress == 270){
                    WideViewValue = 18;
                } else if(progress == 360){
                    WideViewValue = 19;
                }
                Log.d("wide",myDaemon+" "+String.valueOf(WideViewValue));
                ShellUtils.SU(myDaemon +" "+String.valueOf(WideViewValue));
            }
        });
        seekbar_wideview.setProgress(0);
        textview_seekbar.setText(String.valueOf(seekbar_wideview.getProgress()));




        Switch magicbullet = mainView.findViewById(R.id.magicbullet);
        magicbullet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    ShellUtils.SU(myDaemon + " 7");
                } else {
                    ShellUtils.SU(myDaemon + " 8");
                }
            }

        });
        Switch autoheadshot = mainView.findViewById(R.id.autoheadshot);
        autoheadshot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    ShellUtils.SU(myDaemon + " 9");
                } else {
                    ShellUtils.SU(myDaemon + " 10");
                }
            }

        });

        Switch dawnenviorment = mainView.findViewById(R.id.dawnen);
        dawnenviorment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    ShellUtils.SU(myDaemon + " 11");
                } else {
                    ShellUtils.SU(myDaemon + " 12");
                }
            }

        });

        Switch aimbot = mainView.findViewById(R.id.aimbot);
        aimbot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    ShellUtils.SU(myDaemon + " 3");
                } else {
                    ShellUtils.SU(myDaemon + " 4");
                }
            }

        });


        Switch Lessrecoil = mainView.findViewById(R.id.Lessrecoil);
        Lessrecoil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    ShellUtils.SU(myDaemon + " 1");
                } else {
                    ShellUtils.SU(myDaemon + " 2");
                }
            }

        });

        Switch nograss = mainView.findViewById(R.id.nograss);
        nograss.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    ShellUtils.SU(myDaemon + " 13");
                } else {
                    ShellUtils.SU(myDaemon + " 14");
                }
            }

        });




        RadioButton offfog = mainView.findViewById(R.id.offfog);
        RadioButton nofog = mainView.findViewById(R.id.nofog);
        RadioButton voiletfog = mainView.findViewById(R.id.voiletfog);
        RadioButton greenfog = mainView.findViewById(R.id.greenfog);
        RadioButton redfog = mainView.findViewById(R.id.redfog);
        offfog.setChecked(true);
        offfog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View p1) {
                ShellUtils.SU(myDaemon + " 20");
            }
        });
        nofog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View p1) {
                ShellUtils.SU(myDaemon + " 21");
            }
        });
        voiletfog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View p1) {
                ShellUtils.SU(myDaemon + " 22");
            }
        });
        greenfog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View p1) {
                ShellUtils.SU(myDaemon + " 23");
            }
        });
        redfog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View p1) {
                ShellUtils.SU(myDaemon + " 24");
            }
        });

    }

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {
            final View collapsedView = layout_icon_control_view;
            //The root element of the expanded view layout
            final View expandedView = layout_main_view;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = paramsMainView.x;
                        initialY = paramsMainView.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.

                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        paramsMainView.x = initialX + (int) (event.getRawX() - initialTouchX);
                        paramsMainView.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        windowManagerMainView.updateViewLayout(mainView, paramsMainView);
                        return true;
                }
                return false;
            }
        };
    }

    private boolean isViewCollapsed() {
        return mainView == null || layout_icon_control_view.getVisibility() == View.VISIBLE;
    }
    private WindowManager.LayoutParams getParams() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                getLayoutType(),
                getFlagsType(),
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        return params;
    }
    private static int getLayoutType() {
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        return LAYOUT_FLAG;
    }
    private int getFlagsType(){
        int LAYOUT_FLAG = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        return LAYOUT_FLAG;
    }
    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    @Override
    public void onCreate() {
        super.onCreate();
        ShowMainView();
        final File daemon = new File(getFilesDir().getPath() + urlref.nameoflib);
        myDaemon = daemon.toString();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
        if (mainView != null){
            windowManagerMainView.removeView(mainView);
        }
    }

}
