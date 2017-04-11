package com.example.hexinary.onefeedv1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class UserProfile extends AppCompatActivity {
    /* Declare components used in the user profile */

    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);

    }

/* End Google Login */

    public void facebookAuthorize(View v) {
        ProUtils.getInstance().log("authorizing facebook");
    }
    public void twitterAuthorize(View v) {
        ProUtils.getInstance().log("authorizing twitter");
    }
    public void instagramAuthorize(View v) {
        ProUtils.getInstance().log("authorizing instagram");
    }
}
