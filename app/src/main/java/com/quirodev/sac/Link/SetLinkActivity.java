package com.quirodev.sac.Link;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.quirodev.sac.MainActivity;
import com.quirodev.sac.R;

import static com.quirodev.sac.MainActivity.mContext;

/**
 * Created by hj on 2018. 6. 4..
 */

public class SetLinkActivity extends AppCompatActivity{
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    String linkUserData;
    EditText linkUser;
    Button checkName ,sendData;
    String linkUsername,linkUsertime,linktokenID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.link_set);
        setTitle("Link");
        linkUser = (EditText) findViewById(R.id.get_link_user);
        checkName = (Button) findViewById(R.id.set_user_link);

        String username = ((MainActivity) mContext).getName();
        String time = ((MainActivity) mContext).getTime();
        String tokenID = FirebaseInstanceId.getInstance().getToken();

        checkName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linkUsername = linkUser.getText().toString();
                if(linkUsername.equals("")){
                    Toast.makeText(getApplicationContext(),"이름을 제대로 입력해 주세요",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    mReference = mDatabase.getInstance().getReference().child(username).child("wlinkname");
                    mReference.setValue(linkUsername);


                    Toast.makeText(getApplicationContext(), "연동 완료", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });



    }
}