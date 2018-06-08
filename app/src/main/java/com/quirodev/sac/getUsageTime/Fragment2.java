package com.quirodev.sac.getUsageTime;

/**
 * Created by hj on 2018. 5. 8..
 */

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quirodev.sac.MainActivity;
import com.quirodev.sac.R;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Fragment2 extends Fragment implements UsageContract.View {
    public Fragment2() {
    }

    private ProgressBar progressBar;
    private TextView permissionMessage;

    private UsageContract.Presenter presenter;
    private UsageStatAdapter adapter;
    TextView textview;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        setContentView(R.layout.get_daily);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        permissionMessage = (TextView) findViewById(R.id.grant_permission_message);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsageStatAdapter();
        recyclerView.setAdapter(adapter);

        permissionMessage.setOnClickListener(v -> openSettings());

        presenter = new UsagePresenter_D(this, this);
        */
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.get_week, container, false);
        View view2 = inflater.inflate(R.layout.activity_main, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        permissionMessage = (TextView) view.findViewById(R.id.grant_permission_message);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new UsageStatAdapter();
        recyclerView.setAdapter(adapter);
        textview = (TextView) view2.findViewById(R.id.textview);
        permissionMessage.setOnClickListener(v -> openSettings());

        presenter = new UsagePresenter_W(this.getContext(), this);

        return view;
    }

    private void openSettings() {
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgressBar(true);
        presenter.retrieveUsageStats();

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){
            ((MainActivity)getActivity()).setTime(((UsagePresenter_W)presenter).getTotal());
        }
        else{}
        super.setUserVisibleHint(isVisibleToUser);
    }

    public long getTotal() {
        return ((UsagePresenter_W)presenter).getTotal();
    }


    @Override
    public void onUsageStatsRetrieved(List<UsageStatsWrapper> list) {
        showProgressBar(false);
        permissionMessage.setVisibility(GONE);
        adapter.setList(list);
    }

    @Override
    public void onUserHasNoPermission() {
        showProgressBar(false);
        permissionMessage.setVisibility(VISIBLE);
    }

    private void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(VISIBLE);
        } else {
            progressBar.setVisibility(GONE);
        }
    }
}
