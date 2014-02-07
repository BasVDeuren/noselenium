package com.gunit.spacecrack;

import android.app.Application;

import com.facebook.model.GraphUser;

import java.util.List;

/**
 * Created by Dimi on 6/02/14.
 */
public class SpaceCrackApplication extends Application {

    //Logged in Facebook user
    private GraphUser graphUser;

    //Friends of logged in Facebook user
    private List<GraphUser> friends;

    private String accesToken;

    public GraphUser getGraphUser() {
        return graphUser;
    }

    public void setGraphUser(GraphUser graphUser) {
        this.graphUser = graphUser;
    }

    public List<GraphUser> getFriends() {
        return friends;
    }

    public void setFriends(List<GraphUser> friends) {
        this.friends = friends;
    }

    public String getAccesToken() {
        return accesToken;
    }

    public void setAccesToken(String accesToken) {
        this.accesToken = accesToken;
    }
}
