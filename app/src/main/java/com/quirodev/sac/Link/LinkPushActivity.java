package com.quirodev.sac.Link;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import static com.quirodev.sac.MainActivity.mContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by hj on 2018. 6. 4..
 */

public class LinkPushActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    Button send;
    String username, usertime, tokenID;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    String linkuser, sendTokenID, linkuser2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.link_push);
        textView = (TextView) findViewById(R.id.link_user_time);
        editText = (EditText) findViewById(R.id.link_send_msg);
        send = (Button) findViewById(R.id.link_send_push);
        username = ((MainActivity) mContext).getName();
        usertime = ((MainActivity) mContext).getTime();
        tokenID = FirebaseInstanceId.getInstance().getToken();
        Log.i("유저네임", username);

        //LinkData data = new LinkData(username,usertime,tokenID);


        mReference = FirebaseDatabase.getInstance().getReference().child(username).child("wlinkname");
        //mReference.child("time").setValue(usertime);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linkuser = dataSnapshot.getValue().toString();
                if (linkuser.equals("")) {
                    Toast.makeText(getApplicationContext(), "사용자 연동을 먼저 해주세요", Toast.LENGTH_LONG).show();
                    finish();
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

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    if (linkuser.equals("")) {
                        finish();
                    } else {
                        DatabaseReference mRef2 = FirebaseDatabase.getInstance().getReference(linkuser).child("time");
                        mRef2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (dataSnapshot.getValue().toString().equals("")){
                                            finish();
                                        } else
                                            textView.setText(linkuser + "님은 현재까지 " + dataSnapshot.getValue().toString() + " 사용했습니다");
                                    }
                                });
                                Log.i("링크시간", dataSnapshot.getValue().toString());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        Thread thread2 = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    if (linkuser.equals("")) {
                        finish();
                    } else {

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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        thread2.start();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject message = new JSONObject();
                JSONObject notification = new JSONObject();
                try {
                    message.put("to", sendTokenID);
                    message.put("data", notification);
                    notification.put("title", username + " 님의 메시지");
                    notification.put("body", editText.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", message,
                        response -> Log.i("onResponse", "" + response.toString()), new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String response = null;
                        try {
                            response = new String(error.networkResponse.data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.e("Error Response", response);
                        Log.e("MYOBJs", message.toString());
                    }
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
                com.quirodev.sac.Link.MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
            }
        });
    }
}

