package com.quirodev.sac.Ranking;

/**
 * Created by hj on 2018. 5. 24..
 */

public class Data {
    private String username;
    private String time;
    private String tokenID;
    public Data()
    {}

    public Data(String username, String time, String tokenID)
    {
        this.username = username;
        this.time = time;
        this.tokenID = tokenID;
    }

    public String getName() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

}
