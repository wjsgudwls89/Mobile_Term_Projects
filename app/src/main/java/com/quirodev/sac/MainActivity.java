package com.quirodev.sac;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quirodev.sac.Link.LinkMainActivity;
import com.quirodev.sac.Ranking.refreshRank;
import com.quirodev.sac.ScreenLock.setTime;
import com.quirodev.sac.SetAlarm.PushNotification;
import com.quirodev.sac.getUsageTime.UsageContract;


/**
 * Created by hj on 2018. 5. 15..
 */

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    TextView usageText;
    TextView name;
    TextView email;
    TextView linkuser;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    String getlinkuser = "Nobody";
    public static Context mContext;
    Button btn1, btn2, btn3, btn4, btn5, btn6;
    FragmentManager manager = getSupportFragmentManager();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mReference;
    String uid;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();

        //mPager = (ViewPager) findViewById(R.id.main_pager);
        //MainPagerAdapter mAdapter = new MainPagerAdapter(getSupportFragmentManager());
        // mPager.setAdapter(mAdapter);
        //mTab.setupWithViewPager(mPager);


        usageText = (TextView) findViewById(R.id.textview);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.beginTransaction().add(R.id.content_main, new UsageFragment()).commit();
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                btn3.setVisibility(View.GONE);
                btn4.setVisibility(View.GONE);
                btn5.setVisibility(View.GONE);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent =new Intent(getApplicationContext(),setTime.class);
               startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.beginTransaction().replace(R.id.content_main,new refreshRank()).commit();
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                btn3.setVisibility(View.GONE);
                btn4.setVisibility(View.GONE);
                btn5.setVisibility(View.GONE);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PushNotification.class);
                startActivity(intent);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LinkMainActivity.class);
                startActivity(intent);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HowToUseActivity.class);
                startActivity(intent);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name);
        email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
        linkuser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.mainlinkuser);

        mReference = FirebaseDatabase.getInstance().getReference().child(user.getDisplayName()).child("wlinkname");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getlinkuser = dataSnapshot.getValue().toString();
                if (getlinkuser.equals("")){
                    getlinkuser="Nobody";
                }
                if (user != null) {
                    uid = user.getUid();
                    name.setText(user.getDisplayName());
                    email.setText(user.getEmail());
                    linkuser.setText("Link with "+ getlinkuser);
                    Log.d("하잉", "여기 이프문 들어옴?");
                }
                // DatabaseReference ref = FirebaseDatabase.getInstance().getReference(linkuser).child("time");
                //textView.setText(linkuser + ": " + ref + "사용");
                //Log.i("링크시간", dataSnapshot.getValue().toString());
                Log.i("링크유저", getlinkuser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (user != null) {
            uid = user.getUid();
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());
            linkuser.setText("Link with "+ getlinkuser);
            Log.d("하잉", "여기 이프문 들어옴?");
        }
        mContext = this;
    }
    private long pressedTime = 0;



    // 리스너 설정 메소드
    @Override
    public void onBackPressed() {
        View view =  findViewById(R.id.activity_main);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        /*
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        }else {
            backPressedTime = tempTime;

        */}




    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        //first layout 메뉴 버튼이 눌리면 실행됨
        if (id == R.id.Appusage) {
            //content_main을 FirstLayout으로 대체합니다
            manager.beginTransaction().replace(R.id.content_main, new UsageFragment()).commit();
            usageText.setVisibility(View.VISIBLE);
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            btn4.setVisibility(View.GONE);
            btn5.setVisibility(View.GONE);
            //first layout 메뉴 버튼이 눌리면 실행됨
        }
        if (id == R.id.Applock) {
            Intent intent = new Intent(getApplicationContext(), setTime.class);
            startActivity(intent);
        }
        if (id == R.id.Ranking) {
            //content_main을 SecondLayout으로 대체합니다
            manager.beginTransaction().replace(R.id.content_main, new refreshRank()).commit();
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            btn4.setVisibility(View.GONE);
            btn5.setVisibility(View.GONE);
        }
        if (id == R.id.Setalarm) {
            Intent intent = new Intent(getApplicationContext(), PushNotification.class);
            startActivity(intent);
        }
        if (id == R.id.link) {
            Intent intent = new Intent(getApplicationContext(), LinkMainActivity.class);
            startActivity(intent);
        }
        if (id == R.id.logout) {
            signOut();
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);//Drawer를 닫음
        return true;
    }
    public String getName(){return name.getText().toString(); }
    public String getTime(){return usageText.getText().toString();}

    public void setTime(long totalTime) {

        long time = totalTime / 1000;
        long hour = time / 3600;
        long minute = (time - (hour * 3600)) / 60;
        long second = time - (hour * 3600) - (minute * 60);


        usageText.setText(String.format("%02d : %02d : %02d", hour, minute,
                second));
    }
/*
    private void saveData() {
        if(uid==null){
            Intent intent = new Intent(this,LoginActivity.class);
            Toast.makeText(getApplicationContext(),"Database 불러오기 오류 다시 로그인 해주세요",Toast.LENGTH_LONG).show();

            startActivity(intent);
        }
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child(user.getUid());
        String username = name.getText().toString();
        String time = usageText.getText().toString();
        Data data = new Data(username,time);
        mRef.setValue(data);
    }
*/
private void signOut() {
    // Firebase sign out
    mAuth.signOut();
    Toast.makeText(getApplicationContext(),"로그아웃 되었습니다",Toast.LENGTH_LONG).show();
}
}