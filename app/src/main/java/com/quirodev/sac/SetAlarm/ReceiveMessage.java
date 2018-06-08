package com.quirodev.sac.SetAlarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.quirodev.sac.MainActivity;
import com.quirodev.sac.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.support.v4.app.NotificationCompat.DEFAULT_SOUND;
import static android.support.v4.app.NotificationCompat.DEFAULT_VIBRATE;
import static com.quirodev.sac.MainActivity.mContext;

public class ReceiveMessage extends BroadcastReceiver {

    String username,tokenID;
    String linkuser , sendTokenID ;
    private DatabaseReference mReference;

    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;


    @Override
    public void onReceive(Context context, Intent intent) {

        username = ((MainActivity) mContext).getName();
        tokenID = FirebaseInstanceId.getInstance().getToken();

        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, PushNotification.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("SAC")
                .setContentText("핸드폰 사용시간이 지났습니다.")
                .setVibrate(new long[]{100,0,100,0})
                .setDefaults(DEFAULT_SOUND|DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent);

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE );
        PowerManager.WakeLock wakeLock = pm.newWakeLock( PowerManager.SCREEN_DIM_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG" );
        wakeLock.acquire(3000);

        notificationmanager.notify(1, builder.build());

        Thread thread2 = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    DatabaseReference mRef3 = FirebaseDatabase.getInstance().getReference().child(linkuser).child("tokenID");
                    mRef3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            sendTokenID = dataSnapshot.getValue().toString();
                            Log.i("링크토큰", sendTokenID);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        Thread thread3 =  new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3500);
                    JSONObject message = new JSONObject();
                    JSONObject notification = new JSONObject();
                    try {
                        message.put("to", sendTokenID);
                        message.put("data", notification);
                        notification.put("title", "SAC");
                        notification.put("body", linkuser + "님의 핸드폰 사용시간이 만료되었습니다.");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", message,
                            response -> Log.i("onResponse", "" + response.toString()), error -> {
                        String response = null;
                        try {
                            response = new String(error.networkResponse.data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.e("Error Response", response);
                        Log.e("MYOBJs", message.toString());
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            String SERVER_API_KEY = "AAAAhRhBjVY:APA91bEyCm8Y-1y5MqAFAUEzJBOWS9OFIONsWPjDu9ed6ghM-tBC7d1hQUIc3njLKaveI60VFg68Zu2BD-OYOXXVE--8jRYPvwOh4ZRjyB0ppgrkrrnEpLLVOt_-L9SGQwmxMvVA0mo2";
                            Map<String, String> headers = new HashMap<String, String>();
                            headers.put("Authorization", "key=" + SERVER_API_KEY);
                            headers.put("Content-Type", " application/json; charset=UTF-8");
                            return headers;
                        }
                    };
                    MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                }catch(Exception e){e.printStackTrace();}
            }
        };

        mReference = FirebaseDatabase.getInstance().getReference().child(username).child("wlinkname");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linkuser = dataSnapshot.getValue().toString();
                if (linkuser.equals("")){

                }else{
                    thread2.start();
                    thread3.start();
                }
                // DatabaseReference ref = FirebaseDatabase.getInstance().getReference(linkuser).child("time");
                //textView.setText(linkuser + ": " + ref + "사용");
                //Log.i("링크시간", dataSnapshot.getValue().toString());
                Log.i("링크유저", linkuser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}