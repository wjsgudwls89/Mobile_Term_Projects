package com.quirodev.usagestatsmanagersample;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class setTime extends Fragment {
    Button button;
    EditText setTime;
    int time;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.settime,container,false);

        button = (Button) view.findViewById(R.id.button);
        setTime = (EditText) view.findViewById(R.id.settime);

        checkPermission();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("눌림?", "onClick: ");
                time = Integer.parseInt(setTime.getText().toString());
                Intent intent = new Intent(getContext(), AlwaysOnTopService.class);
                intent.putExtra("time",time);
                getContext().startService(intent);
            }
        });
        return view;
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(getContext())) {              // 체크
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getContext().getPackageName()));
                startActivity(intent);
            }
        }
    }
}
