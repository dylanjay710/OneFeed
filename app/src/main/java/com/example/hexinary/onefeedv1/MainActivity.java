package com.example.hexinary.onefeedv1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
//import com.facebook.FacebookSdk;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private GoogleSignInOptions gso;
    private GoogleApiClient mobileGoogleApiClient;
    private final String TAG = "SigninActivity";
    private final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureGoogle();
        configureFacebook();
        configureDatabasehandler();

    }

    public void configureDatabasehandler() {
        DatabaseHandler.configureRequestQueue(getApplicationContext());
    }

    public void configureFacebook() {
        FacebookHandler.initializeCallBackManager();
        FacebookHandler.configureLoginButton((LoginButton)findViewById(R.id.facebook_provided_signin_button));
        FacebookHandler.registerLoginCallback();
        FacebookHandler.configureFacebookAccessTokenTracker();
        FacebookHandler.configureFacebookProfileTracker();
    }

    public void configureGoogle() {
        configureGoogleButtonOnclickListener();
        configureGoogleSignInButton();
        configureGoogleSignInOptions();
        configureMobileGoogleApiClient();
    }

    public void configureGoogleButtonOnclickListener() {
        findViewById(R.id.google_provided_signin_button).setOnClickListener(this);
    }

    public void configureGoogleSignInButton() {

        /* START customize_button Set the dimensions of the sign-in button. */
        SignInButton signInButton = (SignInButton) findViewById(R.id.google_provided_signin_button);
        signInButton.setMinimumHeight(50);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

    }

    public void configureGoogleSignInOptions() {
        if (gso == null) {

            /* START configure sign in, Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN. */
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        }
    }

    public void configureMobileGoogleApiClient() {

        if (mobileGoogleApiClient == null) {

            /* START build_client Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso. */
            mobileGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        }
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mobileGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /* Start signOut */
    public void signOutGoogle(View v) {
        try {
            Auth.GoogleSignInApi.signOut(mobileGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {

                    @Override
                    public void onResult(Status status) {
                        ProUtils.getInstance().log("signing out ahora");

                    }
                }
            );
        } catch (Exception e) {
            ProUtils.getInstance().log("Exception Encountered while signing out of google: " + e);
        }
    }

    /* Start revokeAccess */
    private void revokeGoogleAccess() {

        Auth.GoogleSignInApi.revokeAccess(mobileGoogleApiClient).setResultCallback(

            new ResultCallback<Status>() {

                @Override
                public void onResult(Status status) {

                    updateUI(false);

                }

            }
        );
    }

    /* Start handleSignInResult */
    private void handleSignInResult(GoogleSignInResult result) {

        /* String concatenation means implicit casting ot booleans! */
        ProUtils.getInstance().log("main activity handleSignInResultBeing called");
        ProUtils.getInstance().log("Login Success: " + result.isSuccess());

        if (result.isSuccess()) {

            updateUI(true);
            DatabaseHandler.handleGoogleLogin(result);

            /* Signed in successfully, show authenticated UI. */
//            GoogleSignInAccount acct = result.getSignInAccount();
//            String displayName = acct.getDisplayName();
//            String email = acct.getEmail();
//            String familyName = acct.getFamilyName();
//            String givenName = acct.getGivenName();
//            String id = acct.getId();
//            String idToken = acct.getIdToken();
//            String serverAuthCode = acct.getServerAuthCode();



        } else {

            ProUtils.getInstance().log("User has signed out of google");

        }
    }

    private void showProgressDialog() {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public void updateUI(boolean signedIn) {

        if (signedIn) {

            findViewById(R.id.google_provided_signin_button).setVisibility(View.GONE);
            findViewById(R.id.google_provided_signout_button).setVisibility(View.VISIBLE);

        } else {

//            mStatusTextView.setText("signed out");

            findViewById(R.id.google_provided_signin_button).setVisibility(View.VISIBLE);
            findViewById(R.id.google_provided_signout_button).setVisibility(View.GONE);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProUtils.getInstance().log("main activity onDestroy method being called");
        FacebookHandler.stopTrackingAccessToken();
        FacebookHandler.stopTrackingProfile();
        FacebookHandler.logout();

    }

    @Override
    public void onStart() {

        super.onStart();
        ProUtils.getInstance().log("main activity onStart method running");
        updateUI(false);


        /* this attempts to login users into their google account on activity start, */
//        if (opr.isDone()) {
//
//            ProUtils.getInstance().log("Checking user cached google credentials");
//
//            /* If the user's cached credentials are valid, the OptionalPendingResult will be "done" and the GoogleSignInResult will be available instantly. */
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//
//        } else {
//
//             /* If the user has not previously signed in on this device or the sign-in has expired, this asynchronous branch will attempt to sign in the user silently.  Cross-device single sign-on will occur in this branch. */
//            ProUtils.getInstance().log("User has expired credentials or is signing in for the first time");
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//
//            });
//
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ProUtils.getInstance().log("onActivityResult");

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            ProUtils.getInstance().log("" + requestCode + "," + resultCode);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        } else {

            ProUtils.getInstance().log("Facebook onActivity request code: " + requestCode);
            FacebookHandler.getCallbackManager().onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public void onClick(View v) {
        ProUtils.getInstance().log("main activty onClick running");
        switch (v.getId()) {

            case R.id.google_provided_signin_button:
                signInGoogle();
                break;

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        /* An unresolvable error has occurred and Google APIs (including Sign-In) will not be available. */
        ProUtils.getInstance().log("onConnectionFailed execution");

    }

/* End Google Login */

}
