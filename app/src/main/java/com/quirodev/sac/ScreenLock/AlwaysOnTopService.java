package com.quirodev.sac.ScreenLock;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quirodev.sac.R;

/**
 * Created by hj on 2018. 5. 21..
 */

public class AlwaysOnTopService extends Service {
    LinearLayout view;
    LayoutInflater li;
    TextView tv;
    int time;
    int realtime;

    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.TYPE_PHONE,//항상 최 상위에 있게
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,       //터치 인식
            PixelFormat.RGBA_8888);
    WindowManager wm;

    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("껏다켜지면 여기오긴하니", "응?");
        li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);


        //최상위 윈도우에 넣기 위한 설정
        wm = (WindowManager) getSystemService(WINDOW_SERVICE); //윈도 매니저


        view = (LinearLayout) li.inflate(R.layout.screenlock, null);
        tv = (TextView) view.findViewById(R.id.textview3);

        wm.addView(view, params);
        Log.d("?", "뷰는만들어?");

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                setTime(time);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    time--;
                    if (time <= 0)
                        stopSelf();
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        time = intent.getIntExtra("time",1800);
        return START_REDELIVER_INTENT;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (view != null)        //서비스 종료시 뷰 제거. *중요 : 뷰를 꼭 제거 해야함.
        {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(view);
            view = null;
        }
    }
    private void setTime(int time) {

        long hour = time / 3600;
        long minute = (time - (hour * 3600)) / 60;
        long second = time - (hour * 3600) - (minute * 60);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,40);
        tv.setText(String.format("%02d : %02d : %02d", hour, minute,second));
    }
}