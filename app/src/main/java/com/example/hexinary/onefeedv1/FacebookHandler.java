package com.example.hexinary.onefeedv1;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by hexinary on 17-4-1.
 */

class FacebookHandler {

    private MainActivity mainActivityInstance;
    private UserProfile userProfileInstance;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken currentAccessToken;
    private ProfileTracker profileTracker;
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    public FacebookHandler(MainActivity ma) {

        this.mainActivityInstance = ma;

    }

    public FacebookHandler(UserProfile up) {

        this.userProfileInstance = up;

    }

    public void registerLoginCallback() {

        this.getLoginButton().registerCallback(this.getCallbackManager(), new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                ProUtils.getInstance().log("User has successfully logged in with facebook");
                loadFacebookFeed();
            }

            @Override
            public void onCancel() {
                ProUtils.getInstance().log("User has canceled facebook login");
            }

            @Override
            public void onError(FacebookException exception) {
                ProUtils.getInstance().log("Facebook Exception encountered: " + exception.toString());
            }

        });
    }

    public void initializeCallBackManager() {

        ProUtils.getInstance().log("Setting facebooks callback manager");
        this.callbackManager = CallbackManager.Factory.create();

    }

    public CallbackManager getCallbackManager() {
        return this.callbackManager;
    }

    public void configureLoginButton(MainActivity ma) {

        this.loginButton = (LoginButton) ma.findViewById(R.id.facebook_provided_signin_button);
        this.loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_posts"));

    }

    public void configureFacebookAccessTokenTracker() {


        this.accessTokenTracker = new AccessTokenTracker() {

            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {}

        };

        this.currentAccessToken = AccessToken.getCurrentAccessToken();
        ProUtils.getInstance().log("setting new access token ");

    }

    public void configureFacebookProfileTracker() {

        this.profileTracker = new ProfileTracker() {

            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                ProUtils.getInstance().log("Current facebook profile changed");
                ProUtils.getInstance().log("showing old facebook profile");

                if (oldProfile != null)
                    ProUtils.getInstance().log(oldProfile.toString());
                else
                    ProUtils.getInstance().log("Users facebook old profile is null");

                ProUtils.getInstance().log("showing new facebook profile");

                if (currentProfile != null)
                    ProUtils.getInstance().log(currentProfile.toString());
                else
                    ProUtils.getInstance().log("Users facebook new updated profile is null. Whaaaa nooooo");
            }
        };
    }

    public void loadFacebookFeed() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        ProUtils.getInstance().log("Load facebook feed response");
                        JSONObject fbResponse = response.getJSONObject();
                        ProUtils.getInstance().log(fbResponse.toString());
                        Map<String, String> fbResponseParsed = null;

                        try {
                            fbResponseParsed = parseJsonRecursively(fbResponse, new HashMap<String, String>());
                            for (String key : fbResponseParsed.keySet()) {
                                ProUtils.getInstance().log(key + "," + fbResponseParsed.get(key));
                            }
                        } catch (JSONException e) {

                            ProUtils.getInstance().log("Error parsing fb json");
                            e.printStackTrace();

                        }




                    }
                }
        ).executeAsync();
    }

    public LoginButton getLoginButton() {
        return this.loginButton;
    }

    public void stopTrackingAccessToken() {

        this.accessTokenTracker.stopTracking();

    }

    public Map<String, String> parseJsonRecursively(JSONObject jsonObject, Map<String, String> map) throws JSONException {

        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = null;

            try {
                JSONObject next = jsonObject.getJSONObject(key);
                parseJsonRecursively(next, map);

            } catch (Exception e) {

                if (jsonObject.get(key) instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) jsonObject.get(key);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (jsonArray.get(i) instanceof JSONObject) {
                            parseJsonRecursively((JSONObject) jsonArray.get(i), map);
                        } else if (jsonArray.get(i) instanceof String) {
                            value = jsonObject.getString(key);
                            map.put(key, value);
                        }
                    }
                } else if (jsonObject.get(key) instanceof String) {
                    value = jsonObject.getString(key);
                    map.put(key, value);
                }

            }
        }

        return map;
    }

}
