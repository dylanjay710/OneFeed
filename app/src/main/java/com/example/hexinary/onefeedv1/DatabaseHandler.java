package com.example.hexinary.onefeedv1;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StringDef;
import android.support.annotation.StyleRes;
import android.view.Display;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by hexinary on 17-4-1.
 */

class DatabaseHandler {

    private String handleGoogleLoginUrl = "https://djcodes-onefeed-backend.appspot.com/handle_google_login";
    private String handleFacebookLoginUrl = "https://djcodes-onefeed-backend.appspot.com/handle_facebook_login";
    private String handleCustomLoginUrl = "https://djcodes-onefeed-backend.appspot.com/handle_custom_login";

    private RequestQueue requestQueue;

    public DatabaseHandler(Context context) {

        this.requestQueue = Volley.newRequestQueue(context);

    }

    public void handleGoogleLogin(GoogleSignInResult result) {

        final GoogleSignInAccount acct = result.getSignInAccount();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, this.handleGoogleLoginUrl,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        ProUtils.getInstance().log("Handle Google Sign in Response: " + response);
                    }

                },

                new Response.ErrorListener() { //Create an error listener to handle errors appropriately.

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ProUtils.getInstance().log("Volley Error " + error.toString());
                    }

                }) {

            protected Map<String, String> getParams() {

                return getGoogleUserData(acct);

            }
        };

        this.requestQueue.add(MyStringRequest);

    }

    public void handleFacebookLogin() {
        ProUtils.getInstance().log("handling facebook login");

    }

    public HashMap<String, String> getGoogleUserData(GoogleSignInAccount acct) {

        HashMap<String, String> googleUserdata = new HashMap<>();

        googleUserdata.put("display_name", acct.getDisplayName());
        googleUserdata.put("email", acct.getEmail());
        googleUserdata.put("family_name", acct.getFamilyName());
        googleUserdata.put("given_name", acct.getGivenName());
        googleUserdata.put("google_id", acct.getId());

        // String idToken = acct.getIdToken();
        // String serverAuthCode = acct.getServerAuthCode();

        return googleUserdata;

    }

}