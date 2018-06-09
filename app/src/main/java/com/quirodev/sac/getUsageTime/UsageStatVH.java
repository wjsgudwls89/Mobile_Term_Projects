package com.quirodev.sac.getUsageTime;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quirodev.sac.R;

public class UsageStatVH extends RecyclerView.ViewHolder {

    private ImageView appIcon;
    private TextView appName;
    private TextView gettotaltime;


    public UsageStatVH(View itemView) {
        super(itemView);

        appIcon = (ImageView) itemView.findViewById(R.id.icon);
        appName = (TextView) itemView.findViewById(R.id.title);
        gettotaltime = (TextView) itemView.findViewById(R.id.last_used);

    }

    public void bindTo(UsageStatsWrapper usageStatsWrapper) {

        long time = usageStatsWrapper.getUsageStats().getTotalTimeInForeground()/1000;

        long hour = time/3600;
        long minute = (time-(hour*3600))/60;
        long second = time-(hour*3600)-(minute*60);



        appIcon.setImageDrawable(usageStatsWrapper.getAppIcon());

        appName.setText(usageStatsWrapper.getAppName());

        if (usageStatsWrapper.getUsageStats() == null){
            gettotaltime.setText(R.string.used_never);
        }else if (usageStatsWrapper.getUsageStats().getLastTimeUsed() == 0L){
            gettotaltime.setText(R.string.used_never);
        } else{
            gettotaltime.setText(App.getApp().getString(R.string.get_used_time,
                    String.format("%02d : %02d : %02d", hour,minute,
                            second)));
        }
    }
}
