package com.quirodev.sac.ScreenLock;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.quirodev.sac.R;

public class setTime extends AppCompatActivity {
    Button button;
    //EditText setTime;
    int time;
    int hour = 0, min = 0, sec = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settime);
        button = (Button) findViewById(R.id.button);
        //setTime = (EditText) findViewById(R.id.settime);

        NumberPicker NmHour = (NumberPicker) findViewById(R.id.locksetHour);
        NumberPicker NmMin = (NumberPicker) findViewById(R.id.locksetMin);
        NumberPicker NmSec = (NumberPicker) findViewById(R.id.locksetSec);

        NmHour.setMinValue(0);
        NmHour.setMaxValue(23);
        NmHour.setWrapSelectorWheel(false);
        NmHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        NmMin.setMinValue(0);
        NmMin.setMaxValue(59);
        NmMin.setWrapSelectorWheel(false);
        NmMin.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        NmSec.setMinValue(0);
        NmSec.setMaxValue(59);
        NmSec.setWrapSelectorWheel(false);
        NmSec.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);





        checkPermission();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hour = NmHour.getValue();
                min = NmMin.getValue();
                sec = NmSec.getValue();

                time = hour*3600 + min*60 + sec;
                Log.d("눌림?", "onClick: ");
                Log.d("time",""+time);
                //time = Integer.parseInt(setTime.getText().toString());
                Intent intent = new Intent(getApplicationContext(), AlwaysOnTopService.class);
                intent.putExtra("time",time);
                startService(intent);
            }
        });
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(getApplicationContext())) {              // 체크
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivity(intent);
            }
        }
    }
}
