package br.com.chef2share.domain.request;

/**
 * Created by Jonas on 20/11/2015.
 */
public class LoginGoogle {

    private String userID;
    private String accessToken;
    private Device device;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
