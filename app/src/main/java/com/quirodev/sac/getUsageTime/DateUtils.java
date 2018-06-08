package com.quirodev.sac.getUsageTime;

import java.text.SimpleDateFormat;

public class DateUtils {

    public static String format(UsageStatsWrapper usageStatsWrapper){
        SimpleDateFormat format = new SimpleDateFormat("yy,MM,dd");
        //format = SimpleDateFormat.getDateInstance(DateFormat.SHORT);

        return format.format(usageStatsWrapper.getUsageStats().getLastTimeUsed());
    }
}
