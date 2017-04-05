package com.example.hexinary.onefeedv1;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
//import com.facebook.FacebookSdk;


public class MainActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleHandler googleHandler = new GoogleHandler(this);
    private FacebookHandler facebookHandler = new FacebookHandler(this);

    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureGoogle();
        configureFacebook();
    }

    public void configureFacebook() {
        this.facebookHandler.initializeCallBackManager();
        this.facebookHandler.configureLoginButton(this);
        this.facebookHandler.registerLoginCallback();
    }

    public void configureGoogle() {
        this.googleHandler.setOnClickListenerMainActivity();
        this.googleHandler.configureGoogleSignInOptionsMainActivity();
        this.googleHandler.configureMobileGoogleApiClientMainActivity();
        this.googleHandler.configureGoogleSignInButtonMainActivity();
    }

    public void checkUserLoggedInGoogle() {}

    public void checkUserLoggedInFacebook() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProUtils.getInstance().log("main activity onDestroy method being called");
    }

    @Override
    public void onStart() {
        super.onStart();
        ProUtils.getInstance().log("main activity onStart method running");
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(this.googleHandler.getMobileGoogleApiClient());
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
        if (requestCode == this.googleHandler.getRcSignIn()) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        } else {

            ProUtils.getInstance().log("Facebook onActivity request code: " + requestCode);
            this.facebookHandler.getCallbackManager().onActivityResult(requestCode, resultCode, data);

        }
    }

    /* Start signIn */
    private void signInGoogle() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(this.googleHandler.getMobileGoogleApiClient());
        startActivityForResult(signInIntent, this.googleHandler.getRcSignIn());

    }

    /* Start signOut */
    public void signOutGoogle(View v) {

        this.googleHandler.signOutGoolgeMainActivity();

    }

    public void signInFacebook() {

        ProUtils.getInstance().log("Signing in with facebook");
        this.facebookHandler.signIn();

    }

    /* Start revokeAccess */
    private void revokeAccess() {

        Auth.GoogleSignInApi.revokeAccess(this.googleHandler.getMobileGoogleApiClient()).setResultCallback(

                new ResultCallback<Status>() {

                    @Override
                    public void onResult(Status status) {

                        updateUI(false);

                    }

                });
    }

    /* Start handleSignInResult */
    private void handleSignInResult(GoogleSignInResult result) {

        /* String concatenation means implicit casting ot booleans! */
        ProUtils.getInstance().log("main activity handleSignInResultBeing called");
        ProUtils.getInstance().log("Login Success: " + result.isSuccess());

        if (result.isSuccess()) {

            updateUI(true);
            /* Signed in successfully, show authenticated UI. */
            GoogleSignInAccount acct = result.getSignInAccount();
            String displayName = acct.getDisplayName();
            String email = acct.getEmail();
            String familyName = acct.getFamilyName();
            String givenName = acct.getGivenName();
            String id = acct.getId();
            String idToken = acct.getIdToken();
            String serverAuthCode = acct.getServerAuthCode();

            ProUtils.getInstance().log(
                    "[+]Google Display Name: " + displayName + "\n" +
                            "[+]Google Email: " + email + "\n" +
                            "[+]Google Family Name: " + familyName + "\n" +
                            "[+]Google Given Name: " + givenName + "\n" +
                            "[+]Google id: " + id + "\n" +
                            "[+]Google id token: " + idToken + "\n" +
                            "[+]Google server auth code: " + serverAuthCode);

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
