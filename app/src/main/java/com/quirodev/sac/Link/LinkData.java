package com.quirodev.sac.Link;

/**
 * Created by hj on 2018. 6. 4..
 */

public class LinkData {
    private String username;
    private String time;
    private String tokenID;
    private String linkusername;


    public LinkData()
    {}

    public LinkData(String username, String time, String tokenID,String linkusername)
    {
        this.username = username;
        this.time = time;
        this.tokenID = tokenID;
        this.linkusername = linkusername;


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

    public String getwlinkname() {
        return linkusername;
    }

    public void setlinkuserName(String username) {
        this.linkusername = linkusername;
    }


}
