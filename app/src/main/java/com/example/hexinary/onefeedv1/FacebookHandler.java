package com.example.hexinary.onefeedv1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by hexinary on 17-4-1.
 */

public final class FacebookHandler {


    private static AccessTokenTracker accessTokenTracker;
    private static AccessToken currentAccessToken;
    private static ProfileTracker profileTracker;
    private static CallbackManager callbackManager;
    private static LoginButton loginButton;
    private static LinkedHashMap<String, List<String>> userFeed;


    public static void registerLoginCallback() {
        loginButton.registerCallback(getCallbackManager(), new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                ProUtils.getInstance().log("User has successfully logged in with facebook");
                loadFacebookFeed("/me/feed");

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

    public static void initializeCallBackManager() {
        ProUtils.getInstance().log("Setting facebooks callback manager");
        callbackManager = CallbackManager.Factory.create();
    }


    public static void configureLoginButton(LoginButton fbLoginButton) {
        fbLoginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_posts"));
        loginButton = fbLoginButton;
    }

    public static void configureFacebookAccessTokenTracker() {
        accessTokenTracker = new AccessTokenTracker() {

            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {}

        };
        currentAccessToken = AccessToken.getCurrentAccessToken();
        ProUtils.getInstance().log("setting new access token ");
    }

    public static void configureFacebookProfileTracker() {

        profileTracker = new ProfileTracker() {

            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                ProUtils.getInstance().log("[+] Current facebook profile changed");
                ProUtils.getInstance().log("[+] showing old facebook profile");

                if (oldProfile != null) {
                    ProUtils.getInstance().log(oldProfile.toString());
                    String firstName = checkNull(oldProfile.getFirstName());
                    String lastName = checkNull(oldProfile.getLastName());
                    String middleName = checkNull(oldProfile.getMiddleName());
                    String name = checkNull(oldProfile.getName());
                    String pictureUri = checkNull(oldProfile.getProfilePictureUri(100, 100));
                    String linkUri = checkNull(oldProfile.getLinkUri());
                    String fid = checkNull(oldProfile.getId());

                    /* Update the database with the updated profile data */
                    DatabaseHandler.handleFacebookProfileChanged(firstName, lastName, middleName, name, pictureUri, linkUri, fid);

                    ProUtils.getInstance().log("[+] Showing Old Facebook Profile Data");
                    ProUtils.getInstance().logArgs(new ArrayList<String>(Arrays.asList(
                            "[+] FirstName: " + checkNull(firstName),
                            "[+] LaastName: " + checkNull(lastName),
                            "[+] MiddleName: "+ checkNull(middleName),
                            "[+] Name: " + checkNull(name),
                            "[+] Picture uri: " + checkNull(pictureUri),
                            "[+] LinkUri: " + checkNull(linkUri),
                            "[+] Facebook Id: " + checkNull(fid)
                    )));

                }
                else
                    ProUtils.getInstance().log("[+] Users facebook old profile is null");

                if (currentProfile != null) {
                    ProUtils.getInstance().log(currentProfile.toString());
                    String firstName = checkNull(currentProfile.getFirstName());
                    String lastName = checkNull(currentProfile.getLastName());
                    String middleName = checkNull(currentProfile.getMiddleName());
                    String name = checkNull(currentProfile.getName());
                    String pictureUri = checkNull(currentProfile.getProfilePictureUri(100, 100));
                    String linkUri = checkNull(currentProfile.getLinkUri());
                    String fid = checkNull(currentProfile.getId());

                    /* Update the database with the updated profile data */
                    DatabaseHandler.handleFacebookProfileChanged(firstName, lastName, middleName, name, pictureUri, linkUri, fid);

                    ProUtils.getInstance().log("[+] Showing New Facebook Profile Data");
                    ProUtils.getInstance().logArgs(new ArrayList<String>(Arrays.asList(
                            "[+] FirstName: " + (firstName),
                            "[+] LaastName: " + (lastName),
                            "[+] MiddleName: "+ (middleName),
                            "[+] Name: " + (name),
                            "[+] Picture uri: " + (pictureUri),
                            "[+] LinkUri: " + (linkUri),
                            "[+] Facebook Id: " + (fid)
                    )));
                }
                else
                    ProUtils.getInstance().log("[+] Users facebook new updated profile is null. Whaaaa nooooo");
            }

        };

    }

    private static String checkNull(Object object) {
        if (object == null)
            return "null";
        else
            return (object instanceof String) ? (String) object : object.toString();
    }

    public static void loadFacebookFeed(String url) {


        new GraphRequest(AccessToken.getCurrentAccessToken(), url, null, HttpMethod.GET,
                new GraphRequest.Callback() {

                    public void onCompleted(GraphResponse response) {

                        ProUtils.getInstance().log("Load facebook feed response");
                        JSONObject fbResponse = response.getJSONObject();
                        ProUtils.getInstance().log(fbResponse.toString());

                        try {


                            userFeed = parseJsonRecursively(fbResponse, new LinkedHashMap<String, List<String>>());
                            ProUtils.getInstance().log("Showing user feed data");

                            ProUtils.getInstance().logUserFacebookFeed(userFeed);

                        } catch (JSONException e) {

                            ProUtils.getInstance().log("Error parsing fb json");
                            e.printStackTrace();

                        }
                    }
                }
        ).executeAsync();
    }

    public static LinkedHashMap<String, List<String>> parseJsonRecursively(JSONObject jsonObject, LinkedHashMap<String, List<String>> map) throws JSONException {

        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {

            String key = keys.next();
            String value = null;

            if ( ! map.containsKey(key))
                map.put(key, new ArrayList<String>());

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
                            value = (String) jsonArray.get(i);
                            ProUtils.getInstance().log(key +"," + value);
                            map.get(key).add(value);
                        }
                    }

                } else if (jsonObject.get(key) instanceof String) {
                    value = jsonObject.getString(key);
                    ProUtils.getInstance().log(key +"," + value);
                    map.get(key).add(value);

                } else if (jsonObject.get(key) instanceof Integer) {
                    value = jsonObject.get(key) + "";
                    ProUtils.getInstance().log(key +"," + value);
                    map.get(key).add(value);
                }
            }
        }

        return map;
    }

    public static void stopTrackingProfile() {
        profileTracker.stopTracking();
    }
    public static void stopTrackingAccessToken() {
        accessTokenTracker.stopTracking();
    }
    public static void logout() {
        LoginManager.getInstance().logOut();
    }
    public static CallbackManager getCallbackManager() {
        return callbackManager;
    }


}
