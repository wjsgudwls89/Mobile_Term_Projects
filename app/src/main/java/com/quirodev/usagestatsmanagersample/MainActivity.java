package com.quirodev.usagestatsmanagersample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quirodev.usagestatsmanagersample.Ranking.inputData;
import com.quirodev.usagestatsmanagersample.getUsageTime.UsageContract;

/**
 * Created by hj on 2018. 5. 15..
 */

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    TextView usageText;
    TextView name;
    TextView email;
    Button btn1, btn2, btn3, btn4;
    FragmentManager manager = getSupportFragmentManager();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String uid;
    private FirebaseUser user;
    private UsageContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //mTab = (TabLayout) findViewById(R.id.main_tab);
        //mPager = (ViewPager) findViewById(R.id.main_pager);
        //MainPagerAdapter mAdapter = new MainPagerAdapter(getSupportFragmentManager());
        // mPager.setAdapter(mAdapter);
        //mTab.setupWithViewPager(mPager);


        usageText = (TextView) findViewById(R.id.textview);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.beginTransaction().add(R.id.content_main, new UsageFragment()).commit();
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                btn3.setVisibility(View.GONE);
                btn4.setVisibility(View.GONE);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.beginTransaction().add(R.id.content_main, new setTime()).commit();
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                btn3.setVisibility(View.GONE);
                btn4.setVisibility(View.GONE);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // manager.beginTransaction().replace(R.id.content_main,new UsageFragment()).commit();
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                btn3.setVisibility(View.GONE);
                btn4.setVisibility(View.GONE);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // manager.beginTransaction().replace(R.id.content_main,new UsageFragment()).commit();
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                btn3.setVisibility(View.GONE);
                btn4.setVisibility(View.GONE);
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

        if (user != null) {
            uid = user.getUid();
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());
            Log.d("하잉", "여기 이프문 들어옴?");
        }

        saveData();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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

            //first layout 메뉴 버튼이 눌리면 실행됨
        }
        if (id == R.id.Applock) {
            manager.beginTransaction().replace(R.id.content_main, new setTime()).commit();
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            btn4.setVisibility(View.GONE);
            usageText.setVisibility(View.GONE);
        }
        if (id == R.id.Ranking) {
            //content_main을 SecondLayout으로 대체합니다
            manager.beginTransaction().replace(R.id.content_main, new inputData()).commit();
        }
/*
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
*/
        if (id == R.id.logout) {
            mAuth.signOut();
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);//Drawer를 닫음
        return true;
    }

    public void setTime(long totalTime) {

        long time = totalTime / 1000;
        long hour = time / 3600;
        long minute = (time - (hour * 3600)) / 60;
        long second = time - (hour * 3600) - (minute * 60);


        usageText.setText(String.format("%02d : %02d : %02d", hour, minute,
                second));
    }

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





}