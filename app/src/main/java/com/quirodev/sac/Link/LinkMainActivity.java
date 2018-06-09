package com.quirodev.sac.Link;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.quirodev.sac.R;

/**
 * Created by hj on 2018. 6. 4..
 */

public class LinkMainActivity extends AppCompatActivity {
    Button setLink,watchLink;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.link_main);
        setTitle("Link");

        setLink = (Button) findViewById(R.id.set_link);
        watchLink = (Button)findViewById(R.id.link_push_button);

        setLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SetLinkActivity.class);
                startActivity(intent);
            }
        });
        watchLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplicationContext(),LinkPushActivity.class);
                startActivity(intent);
            }
        });
    }
}
