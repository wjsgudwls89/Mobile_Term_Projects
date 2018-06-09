package com.quirodev.sac.ScreenLock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by hj on 2018. 5. 28..
 */

public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            Log.d("여기 들어오긴해????", "부트리시버 응? 씨발?");
            Intent i = new Intent(context, AlwaysOnTopService.class);
            context.startService(i);
        }

    }
}
