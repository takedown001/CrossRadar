package com.Gcc.infinity;

import android.app.Service;
import android.os.IBinder;
import android.content.Intent;
import android.os.PowerManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.WindowManager;


import android.os.Build;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.RadioButton;
import java.io.File;


import android.widget.Button;
import android.widget.Switch;
import android.widget.CompoundButton;

import com.Gcc.infinity.GccConfig.urlref;



public class BrutalService extends Service {

    private PowerManager.WakeLock mWakeLock;

    public static final String LOG_TAG = "takedown";

    private View mainView;

    private WindowManager windowManagerMainView;

    private WindowManager.LayoutParams paramsMainView;

    private LinearLayout layout_main_view;

    private RelativeLayout layout_icon_control_view;

    private int WideViewValue = 360;
    private int LessrecoilValue = 220;
    private int FlashValue = 600;
    private String myDaemon = "./"+ urlref.pathoflib+urlref.nameoflib;

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
        mainView = LayoutInflater.from(this).inflate(R.layout.activity_brutalservice, null);
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
        layout_close_main_view.setOnClickListener(new OnClickListener(){
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
        btn_exit.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View p1) {
                stopSelf();
            }
        });


        TextView tv_ric = mainView.findViewById(R.id.textview_ric);
        SeekBar seekbar_wideview = mainView.findViewById(R.id.seekbar_wideview);
        final TextView textview_seekbar = mainView.findViewById(R.id.textview_wideview);
        SeekBar seekbar_Lessrecoil = mainView.findViewById(R.id.seekbar_Lessrecoil);
        final TextView textview_seekbar1 = mainView.findViewById(R.id.textview_Lessrecoil);
        SeekBar seekbar_flash = mainView.findViewById(R.id.seekbar_flash);
        final TextView textview_flash = mainView.findViewById(R.id.textview_flash);

        tv_ric.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View p1) {
                AlertDialog alertDialog = new AlertDialog.Builder(getBaseContext())
                        .setTitle("Contact US")
                        .setMessage("Gcc-org.com/about/")
                        .setPositiveButton("OK", null)
                        .create();

                alertDialog.getWindow().setType(getLayoutType());
                alertDialog.getWindow().setGravity(Gravity.TOP);
                alertDialog.show();
            }
        });
        Switch bypass = mainView.findViewById(R.id.bypass);
        bypass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    ShellUtils.SU(myDaemon + " 101");
                    ShellUtils.SU(myDaemon + " 1111");
                } else {
                    ShellUtils.SU(myDaemon + " 102");
                    ShellUtils.SU(myDaemon + " 2222");
                }
            }
        });


        RadioButton aimoff = mainView.findViewById(R.id.aimoff);
        RadioButton aimbody = mainView.findViewById(R.id.aimbody);
        RadioButton aimhead = mainView.findViewById(R.id.aimhead);

        aimoff.setChecked(true);
        aimoff.setOnClickListener(new OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          ShellUtils.SU(myDaemon + " 4");
                                      }
                                  });
        aimbody.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ShellUtils.SU(myDaemon + " 26");
            }
        });
        aimhead.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ShellUtils.SU(myDaemon + " 3");
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
        seekbar_wideview.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
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
                    WideViewValue =16 ;
                } else if(progress == 180){
                    WideViewValue =17 ;
                } else if(progress == 270){
                    WideViewValue = 18;
                } else if(progress == 360){
                    WideViewValue = 19;
                }
                ShellUtils.SU(myDaemon +" " +String.valueOf(WideViewValue));
            }
        });
        seekbar_wideview.setProgress(0);
        textview_seekbar.setText(String.valueOf(seekbar_wideview.getProgress()));




        seekbar_flash.incrementProgressBy(300);
        seekbar_flash.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar p1, int progress, boolean p3) {
                progress = progress / 300;
                progress = progress * 300;
                textview_flash.setText(String.valueOf(progress));


            }

            @Override
            public void onStartTrackingTouch(SeekBar p1) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar p1) {
                int progress = p1.getProgress();
                progress = progress / 300;
                progress = progress * 300;
                if (progress == 0){
                    FlashValue = 31;
                } else if(progress == 300){
                    FlashValue = 32;
                }  else if(progress == 600){
                    FlashValue = 33;
                }
                ShellUtils.SU(myDaemon + " "+String.valueOf(FlashValue));
            }
        });
        seekbar_flash.setProgress(0);
        textview_flash.setText(String.valueOf(seekbar_flash.getProgress()));


        seekbar_Lessrecoil.incrementProgressBy(50);
        seekbar_Lessrecoil.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar p1, int progress, boolean p3) {
                progress = progress / 50;
                progress = progress * 50;
                textview_seekbar1.setText(String.valueOf(progress));


            }

            @Override
            public void onStartTrackingTouch(SeekBar p1) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar p1) {
                int progress = p1.getProgress();
                progress = progress / 50;
                progress = progress * 50;
                if (progress == 0){
                    LessrecoilValue = 2;
                } else if(progress == 50){
                    LessrecoilValue = 1;
                }  else if(progress == 100){
                    LessrecoilValue = 25;
                }
                ShellUtils.SU(myDaemon  + " "+String.valueOf(LessrecoilValue));
            }
        });
        seekbar_Lessrecoil.setProgress(0);
        textview_seekbar1.setText(String.valueOf(seekbar_Lessrecoil.getProgress()));



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


        Switch downevbr = mainView.findViewById(R.id.dawnevbr);
        downevbr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    ShellUtils.SU(myDaemon + " 11");
                } else {
                    ShellUtils.SU(myDaemon + " 12");
                }
            }

        });

        Switch longjump = mainView.findViewById(R.id.longjump);
        longjump.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    ShellUtils.SU(myDaemon + " 27");
                } else {
                    ShellUtils.SU(myDaemon + " 28");
                }
            }

        });

        Switch speedprone = mainView.findViewById(R.id.speedprone);
        speedprone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    ShellUtils.SU(myDaemon + " 29");
                } else {
                    ShellUtils.SU(myDaemon + " 30");
                }
            }

        });



        RadioButton offfog = mainView.findViewById(R.id.offfog);
        RadioButton nofog = mainView.findViewById(R.id.nofog);
        RadioButton voiletfog = mainView.findViewById(R.id.voiletfog);
        RadioButton greenfog = mainView.findViewById(R.id.greenfog);
        RadioButton redfog = mainView.findViewById(R.id.redfog);
        offfog.setChecked(true);
        offfog.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View p1) {
                ShellUtils.SU(myDaemon + " 20");
            }
        });
        nofog.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View p1) {
                ShellUtils.SU(myDaemon + " 21");
            }
        });
        voiletfog.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View p1) {
                ShellUtils.SU(myDaemon + " 22");
            }
        });
        greenfog.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View p1) {
                ShellUtils.SU(myDaemon + " 23");
            }
        });
        redfog.setOnClickListener(new OnClickListener(){
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
        ShellUtils.SU("chmod 777 "+myDaemon);

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
