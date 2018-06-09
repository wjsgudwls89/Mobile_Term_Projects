package com.quirodev.sac.SetAlarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.quirodev.sac.MainActivity.mContext;

public class PushNotification extends AppCompatActivity {

    Button send;
    EditText user;
    String username,usertime,tokenID;
    String linkuser , sendTokenID , linkuser2;
    private DatabaseReference mReference;
    int hour = 0, min = 0, sec = 0 ,h,m,s;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.push_noti);
        //user = (EditText) findViewById(R.id.name);
        send = (Button) findViewById(R.id.push);
        username = ((MainActivity) mContext).getName();
        tokenID = FirebaseInstanceId.getInstance().getToken();

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow= new SimpleDateFormat("HHmmss");
        String formatDate = sdfNow.format(date);

        h = Integer.parseInt(formatDate.substring(0,2));
        m = Integer.parseInt(formatDate.substring(2,4));
        s = Integer.parseInt(formatDate.substring(4,6));

        NumberPicker NmHour = (NumberPicker) findViewById(R.id.setHour);
        NumberPicker NmMin = (NumberPicker) findViewById(R.id.setMin);
        NumberPicker NmSec = (NumberPicker) findViewById(R.id.setSec);

        NmHour.setMinValue(0);
        NmHour.setMaxValue(23);
        NmHour.setWrapSelectorWheel(false);
        NmHour.setValue(h);
        NmHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        NmMin.setMinValue(0);
        NmMin.setMaxValue(59);
        NmMin.setWrapSelectorWheel(false);
        NmMin.setValue(m);
        NmMin.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        NmSec.setMinValue(0);
        NmSec.setMaxValue(59);
        NmSec.setWrapSelectorWheel(false);
        NmSec.setValue(s);
        NmSec.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        //user = (EditText) findViewById(R.id.name);

        mReference = FirebaseDatabase.getInstance().getReference().child(username).child("wlinkname");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linkuser = dataSnapshot.getValue().toString();
                // DatabaseReference ref = FirebaseDatabase.getInstance().getReference(linkuser).child("time");
                //textView.setText(linkuser + ": " + ref + "사용");
                //Log.i("링크시간", dataSnapshot.getValue().toString());
                Log.i("링크유저", linkuser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        Thread thread3 = new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(3500);
                    sendNoti();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };


        send.setOnClickListener(view -> {
            hour = NmHour.getValue();
            min = NmMin.getValue();
            sec = NmSec.getValue();

            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    linkuser = dataSnapshot.getValue().toString();
                    if(linkuser.equals("")){
                        new AlarmHATT(getApplicationContext()).Alarm();
                        Toast.makeText(getApplicationContext(), "알람이 설정되었습니다.",Toast.LENGTH_SHORT).show();
                    }else{
                        thread2.start();
                        new AlarmHATT(getApplicationContext()).Alarm();
                        Toast.makeText(getApplicationContext(), "알람이 설정되었습니다.",Toast.LENGTH_SHORT).show();
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
        });

    }
    public class AlarmHATT {
        private Context context;

        public AlarmHATT(Context context) {
            this.context = context;
        }

        public void Alarm() {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(PushNotification.this, ReceiveMessage.class);

            PendingIntent sender = PendingIntent.getBroadcast(PushNotification.this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            //알람시간 calendar에 set해주기

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), hour, min, sec);

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdfNow= new SimpleDateFormat("HH:mm:ss");
            String formatDate = sdfNow.format(date);
            Log.i("현재시간", "gkgk"+formatDate);
            Log.d("시간", hour+"시"+min+"분"+sec+"초");
            //알람 예약
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }
    }

    private void sendNoti() {

        JSONObject message = new JSONObject();
        JSONObject notification = new JSONObject();
        try {
            message.put("to", sendTokenID);
            message.put("data", notification);
            notification.put("title", "SAC");
            notification.put("body", linkuser + "님이." + hour + "시" + min + "분" + sec + "초 까지 핸드폰을 사용합니다.");

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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    }



